package com.darkliu.mskin;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author liuting
 */
public class ViewRootCompat {

    @NonNull
    private final View mRoot;
    @Nullable
    private final ISkinResource mResource;
    private final SparseArray<View> mViewSparseArray = new SparseArray<>();

    ViewRootCompat(@NonNull View root, @Nullable ISkinResource resource) {
        mRoot = root;
        mResource = resource;
    }

    public View root() {
        return mRoot;
    }

    public <T extends View> T get(@IdRes int id) {
        View view = mViewSparseArray.get(id);
        if (view == null) {
            view = mRoot.findViewById(id);
            mViewSparseArray.put(id, view);
        }
        return (T) view;
    }
}
