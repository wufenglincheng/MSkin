package com.darkliu.mskin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class DynamicSkinResource implements ISkinResource {

    final static String DEFAULT_KEY = "com.darkliu.mskin.DefaultResources";
    private ISkinResource mSkinResource;

    public void setSkinResource(ISkinResource resource) {
        mSkinResource = resource;
    }

    @Override
    public int getColor(Context context, int id) {
        if (mSkinResource != null) {
            return mSkinResource.getColor(context, id);
        }
        return getNonNullContext(context).getResources().getColor(id);
    }

    @Nullable
    @Override
    public Drawable getDrawable(Context context, int id) {
        if (mSkinResource != null) {
            return mSkinResource.getDrawable(context, id);
        }
        return getNonNullContext(context).getResources().getDrawable(id);
    }

    @NonNull
    @Override
    public View getLayout(Context context, int id, ViewGroup root, boolean attachToRoot) {
        if (mSkinResource != null) {
            return mSkinResource.getLayout(context, id, root, attachToRoot);
        }
        return LayoutInflater.from(context).inflate(id, root, attachToRoot);
    }

    @Override
    public int getId(int id) {
        if (mSkinResource != null) {
            return mSkinResource.getId(id);
        }
        return id;
    }

    @NonNull
    private Context getNonNullContext(Context context) {
        Context finalContext = context;
        if (finalContext == null) {
            finalContext = SkinManager.instance().getApplicationContext();
        }
        return finalContext;
    }

    @Override
    public String getUniqueKey() {
        if (mSkinResource != null) {
            return mSkinResource.getUniqueKey();
        }
        return DEFAULT_KEY;
    }
}
