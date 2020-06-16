package com.darkliu.mskin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;


/**
 * 主题适配器，用在需要主题的Activity里，会替换掉Activity的LayoutInflater的Factory
 *
 * @author liuting
 */
public class SkinContext implements IThemeUpdate {
    private static final String TAG = "SkinContext";
    private static Field sLayoutInflaterFactory2Field;
    private static boolean sCheckedField;
    private final Activity mContext;
    private BaseSkinLayoutInflater mSkinLayoutInflater;
    private final List<IAttrAnim> mAnimList = new ArrayList<>();
    private ValueAnimator mValueAnimator;

    private SkinContext(Activity context, BaseSkinLayoutInflater inflater) {
        mSkinLayoutInflater = inflater;
        mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final LayoutInflater.Factory factory = layoutInflater.getFactory();
        layoutInflater.setFactory2(mSkinLayoutInflater);
        if (factory instanceof LayoutInflater.Factory2) {
            factorySet(layoutInflater, (LayoutInflater.Factory2) factory);
        } else {
            factorySet(layoutInflater, mSkinLayoutInflater);
        }
        SkinManager.instance().attach(this);
    }

    public void destroy() {
        SkinManager.instance().detach(this);
    }

    private void factorySet(LayoutInflater inflater, LayoutInflater.Factory2 factory2) {
        if (!sCheckedField) {
            try {
                sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "forceSetFactory2 Could not find field 'mFactory2' on class "
                        + LayoutInflater.class.getName()
                        + "; inflation may have unexpected results.", e);
            }
            sCheckedField = true;
        }
        if (sLayoutInflaterFactory2Field != null) {
            try {
                sLayoutInflaterFactory2Field.set(inflater, factory2);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "forceSetFactory2 could not set the Factory2 on LayoutInflater "
                        + inflater + "; inflation may have unexpected results.", e);
            }
        }
    }

    @Override
    public void onThemeUpdate(boolean isFromUser) {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        mAnimList.clear();
        applyViewTheme(mContext.getWindow().getDecorView(), mAnimList, isFromUser);
        if (!mAnimList.isEmpty()) {
            mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
            mValueAnimator.setDuration(150);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    for (int i = 0; i < mAnimList.size(); i++) {
                        IAttrAnim attrAnim = mAnimList.get(i);
                        attrAnim.onValueChange(value);
                    }
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimList.clear();
                    mValueAnimator = null;
                }
            });
            mValueAnimator.start();
        }
    }

    private void applyViewTheme(View view, List<IAttrAnim> animList, boolean isFromUser) {
        Object o = view.getTag(R.id.skin_processor_tag);
        if (o instanceof ViewThemeProcessor) {
            ((ViewThemeProcessor) o).apply(isFromUser, animList);
        }
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                applyViewTheme(((ViewGroup) view).getChildAt(i), animList, isFromUser);
            }
        }
    }

    /**
     * 创建基于FragmentActivity的SkinContext
     *
     * @param context
     * @param delegate
     * @return
     */
    public static SkinContext instance(Activity context, FragmentActivity delegate) {
        return new SkinContext(context, new DefaultSkinLayoutInflater(delegate));
    }

    /**
     * 创建基于AppcompatActivity的SkinContext
     *
     * @param context
     * @param delegate
     * @return
     */
    public static SkinContext instance(Activity context, AppCompatDelegate delegate) {
        return new SkinContext(context, new AppCompatSkinLayoutInflater(delegate));
    }
}
