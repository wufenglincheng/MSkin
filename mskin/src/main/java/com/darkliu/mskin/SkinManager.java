package com.darkliu.mskin;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.core.util.Consumer;

/**
 * 主题管理类
 *
 * @author liuting
 */
public class SkinManager {
    private final static String TAG = "SkinManager";
    private final static SkinManager INSTANCE = new SkinManager();

    public static SkinManager instance() {
        return INSTANCE;
    }

    public static ISkinResource resource() {
        return INSTANCE.mSkinResource;
    }

    public static <T extends View> void applySkinColor(T view, TypedArray array, @StyleableRes int index, @ColorInt int defColor, final Consumer<Integer> consumer) {
        int id = array.getResourceId(index, 0);
        if (id > 0) {
            applySkinColor(view, id, consumer);
            return;
        }
        consumer.accept(array.getColor(index, defColor));
    }

    public static <T extends View> void applySkinDrawable(T view, TypedArray array, @StyleableRes int index, final Consumer<Drawable> consumer) {
        int id = array.getResourceId(index, 0);
        if (id > 0) {
            applySkinDrawable(view, id, consumer);
            return;
        }
        consumer.accept(array.getDrawable(index));
    }

    public static <T extends View> void applySkinColor(T view, @ColorRes final int color, final Consumer<Integer> consumer) {
        ViewThemeProcessor processor = getProcessor(view);
        CustomSkinAttrApply<Integer> apply = new CustomSkinAttrApply<>(color, resource()::getColor, consumer);
        processor.add(apply);
        apply.apply(view);
    }

    public static <T extends View> void applySkinDrawable(T view, @DrawableRes final int drawable, final Consumer<Drawable> consumer) {
        ViewThemeProcessor processor = getProcessor(view);
        CustomSkinAttrApply<Drawable> apply = new CustomSkinAttrApply<>(drawable, resource()::getDrawable, consumer);
        processor.add(apply);
        apply.apply(view);
    }

    @NonNull
    protected static ViewThemeProcessor getProcessor(View view) {
        Object o = view.getTag(R.id.skin_processor_tag);
        if (o instanceof ViewThemeProcessor) {
            return (ViewThemeProcessor) o;
        }
        ViewThemeProcessor processor = new ViewThemeProcessor(view);
        view.setTag(R.id.skin_processor_tag, processor);
        return processor;
    }

    private final List<IThemeUpdate> mThemeUpdates = new ArrayList<>();
    private Context mApplicationContext;
    private DynamicSkinResource mSkinResource = new DynamicSkinResource();
    private LoadExpandThemeTask mThemeTask;

    public void init(@NonNull Application application) {
        mApplicationContext = application;
    }

    @NonNull
    protected Context getApplicationContext() {
        if (mApplicationContext == null) {
            throw new RuntimeException("SkinManager.init must be call");
        }
        return mApplicationContext;
    }

    /**
     * 设置扩展主题
     *
     * @param path  扩展主题路径
     * @param state 切换状态监听
     */
    public void switchToExpandTheme(final String path, @Nullable final IThemeChangeState state) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (path.equals(mSkinResource.getUniqueKey())) {
            return;
        }
        if (mThemeTask != null) {
            mThemeTask.cancel(true);
        }
        mThemeTask = new LoadExpandThemeTask(state);
        mThemeTask.execute(path);
    }

    /**
     * 切换到默认的主题
     */
    public void switchToDefaultTheme() {
        if (DynamicSkinResource.DEFAULT_KEY.equals(mSkinResource.getUniqueKey())) {
            return;
        }
        mSkinResource.setSkinResource(null);
        notifyThemeUpdateEvent();
    }

    public boolean isDefault() {
        return DynamicSkinResource.DEFAULT_KEY.equals(mSkinResource.getUniqueKey());
    }

    public void attach(IThemeUpdate update) {
        mThemeUpdates.add(update);
    }

    public void detach(IThemeUpdate update) {
        mThemeUpdates.remove(update);
    }

    private void setExpandResource(ExpandResources result) {
        if (result == null) {
            return;
        }
        mSkinResource.setSkinResource(result);
        notifyThemeUpdateEvent();
    }

    private void notifyThemeUpdateEvent() {
        for (IThemeUpdate update : mThemeUpdates) {
            update.onThemeUpdate(true);
        }
    }

    /**
     * 切换主题的异步任务
     */
    @SuppressLint("StaticFieldLeak")
    private class LoadExpandThemeTask extends AsyncTask<String, Void, ExpandResources> {

        private final WeakReference<IThemeChangeState> mIThemeChangeStateWeakRe;

        LoadExpandThemeTask(IThemeChangeState state) {
            this.mIThemeChangeStateWeakRe = new WeakReference<>(state);
        }

        @Override
        protected ExpandResources doInBackground(String... paths) {
            try {
                String skinPath = paths[0];
                File file = new File(skinPath);
                if (!file.exists()) {
                    return null;
                }
                return loadExpandResources(skinPath);
            } catch (Exception e) {
                Log.e(TAG, "append expandResources(" + paths[0] + ") fail", e);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mIThemeChangeStateWeakRe.clear();
            mThemeTask = null;
        }

        @Override
        protected void onPreExecute() {
            if (mIThemeChangeStateWeakRe.get() == null) {
                return;
            }
            mIThemeChangeStateWeakRe.get().onStart();
        }

        @Override
        protected void onPostExecute(ExpandResources result) {
            setExpandResource(result);
            mThemeTask = null;
            if (mIThemeChangeStateWeakRe.get() == null) {
                return;
            }
            if (result != null) {
                mIThemeChangeStateWeakRe.get().onChangeFinish();
            } else {
                mIThemeChangeStateWeakRe.get().onError();
            }
            mIThemeChangeStateWeakRe.clear();
        }
    }

    /**
     * 根据路径得到扩展资源
     *
     * @param skinPath
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Nullable
    private ExpandResources loadExpandResources(String skinPath) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        File file = new File(skinPath);
        if (!file.exists()) {
            return null;
        }
        PackageManager packageManager = getApplicationContext().getPackageManager();
        // 通过包管理获取未安装应用（即apk）的信息
        PackageInfo packageInfo =
                packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);
        Resources superResource = getApplicationContext().getResources();
        Resources skinResource = new Resources(assetManager, superResource.getDisplayMetrics(),
                superResource.getConfiguration());
        return new ExpandResources(getApplicationContext(), skinResource, packageInfo.packageName, skinPath);
    }
}
