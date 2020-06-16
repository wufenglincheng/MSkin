package com.darkliu.mskin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author liuting
 */
public interface ISkinResource {

    String DRAWABLE = "drawable";
    String COLOR = "color";
    String LAYOUT = "layout";
    String ID = "id";

    /**
     * 获取颜色
     *
     * @param context
     * @param id
     * @return
     */
    @ColorInt
    int getColor(Context context, @ColorRes int id);

    /**
     * 获取Drawable资源
     *
     * @param context
     * @param id
     * @return
     */
    @Nullable
    Drawable getDrawable(Context context, @DrawableRes int id);

    /**
     * 获取layout资源
     * @param context
     * @param id
     * @param root
     * @param attachToRoot
     * @return
     */
    @NonNull
    View getLayout(Context context, @LayoutRes int id, ViewGroup root, boolean attachToRoot);

    /**
     * 获取id
     * @param id
     * @return
     */
    int getId(@IdRes int id);

    /**
     * 返回唯一标识
     * @return
     */
    String getUniqueKey();
}
