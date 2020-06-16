package com.darkliu.mskin;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author liuting
 */
public class ExpandResources implements ISkinResource {

    private static final String TAG = "ExpandResources";
    @NonNull
    private final Context mContext;
    @NonNull
    private final Resources mResources;
    @NonNull
    private final String mPackName;
    @NonNull
    private final String mSkinPath;

    public ExpandResources(@NonNull Context context, @NonNull Resources resources, @NonNull String packName, @NonNull String skinPath) {
        this.mContext = context;
        this.mResources = resources;
        this.mPackName = packName;
        this.mSkinPath = skinPath;
    }

    /**
     * 获取颜色资源
     *
     * @param context 传入的Context
     * @param attrValueRefId 资源ID
     * @return 色值
     */
    @Override
    public int getColor(Context context, int attrValueRefId) {
        String name = getNonNullContext(context).getResources().getResourceEntryName(attrValueRefId);
        int trueId = mResources.getIdentifier(name, COLOR, mPackName);
        boolean find = false;
        int trueColor = 0;
        if (trueId != 0) {
            try {
                trueColor = mResources.getColor(trueId);
                find = true;
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "no find color(" + name + ") in " + mPackName, e);
            }
        }
        if (!find) {
            notFindResource(name);
            trueColor = context.getResources().getColor(attrValueRefId);
        }
        return trueColor;
    }

    /**
     * 获取drawable资源
     *
     * @param context
     * @param attrValueRefId 资源ID
     * @return drawable
     */
    @Nullable
    @Override
    public Drawable getDrawable(Context context, int attrValueRefId) {
        String name = mContext.getResources().getResourceEntryName(attrValueRefId);
        int trueId = mResources.getIdentifier(name, DRAWABLE, mPackName);
        Drawable trueDrawable = null;
        if (trueId != 0) {
            try {
                trueDrawable = mResources.getDrawable(trueId);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "no find drawable(" + name + ") in " + mPackName, e);
            }
        }
        if (trueDrawable == null) {
            notFindResource(name);
            trueDrawable = mContext.getResources().getDrawable(attrValueRefId);
        }
        return trueDrawable;
    }

    @NonNull
    @Override
    public View getLayout(Context context, int attrValueRefId, ViewGroup root, boolean attachToRoot) {
        View view = null;
        XmlResourceParser parser = null;
        String name = mContext.getResources().getResourceEntryName(attrValueRefId);
        int trueId = mResources.getIdentifier(name, LAYOUT, mPackName);
        try {
            parser = mResources.getLayout(trueId);
            view = LayoutInflater.from(getNonNullContext(context)).inflate(parser, root, attachToRoot);
            mapViewIdToLocal(view);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "no find layout(" + name + ") in " + mPackName, e);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
        if (view == null) {
            view = LayoutInflater.from(getNonNullContext(context)).inflate(attrValueRefId, root, attachToRoot);
        }
        return view;
    }


    @Override
    public int getId(int attrValueRefId) {
        String name = mContext.getResources().getResourceEntryName(attrValueRefId);
        return mResources.getIdentifier(name, ID, mPackName);
    }


    /**
     * 替换view的id为本地id
     * @param view
     */
    public void mapViewIdToLocal(View view) {
        setViewIdToLocal(view);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                mapViewIdToLocal(group.getChildAt(i));
            }
        }
    }

    public void setViewIdToLocal(View v) {
        if (v.getId() == View.NO_ID) {
            return;
        }
        String name = mResources.getResourceEntryName(v.getId());
        int id = mContext.getResources().getIdentifier(name, ID, mContext.getPackageName());
        v.setId(id);
    }

    @NonNull
    private Context getNonNullContext(Context context) {
        Context finalContext = context;
        if (finalContext == null) {
            finalContext = mContext;
        }
        return finalContext;
    }

    @Override
    public String getUniqueKey() {
        return mSkinPath;
    }

    private void notFindResource(String resName) {
        Log.i(TAG, "在扩展主题(" + mPackName + ")里未找到资源 " + resName);
    }
}
