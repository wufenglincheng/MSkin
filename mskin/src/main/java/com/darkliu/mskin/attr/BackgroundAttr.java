package com.darkliu.mskin.attr;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;
import com.darkliu.mskin.anim.AnimBackgroundDrawable;
import com.darkliu.mskin.anim.ColorDrawableAttrAnim;

import java.util.List;


/**
 * 通用属性背景的主题替换类
 *
 * @author liuting
 */
public class BackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (ISkinResource.COLOR.equals(attrInfo.attrValueTypeName)) {
            int currentColor = SkinManager.resource().getColor(view.getContext(), attrInfo.attrValueRefId);
            // 如果是来自用户主动切换可以触发背景动画
            if (isFormUser && view.getBackground() instanceof ColorDrawable) {
                // 如果view的背景是ColorDrawable，那么可以做背景渐变动画
                int start = ((ColorDrawable) view.getBackground()).getColor();
                ColorDrawable colorDrawable = new ColorDrawable(currentColor);
                if (start != currentColor && attrAnimationList != null) {
                    colorDrawable.setColor(start);
                    attrAnimationList.add(new ColorDrawableAttrAnim(colorDrawable, start, currentColor));
                }
                view.setBackground(colorDrawable);
            } else {
                view.setBackgroundColor(currentColor);
            }
        } else if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName)) {
            Drawable bg = SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId);
            Drawable lastDrawable = view.getBackground();
            if (isFormUser && lastDrawable != null) {
                if (bg instanceof StateListDrawable
                        && lastDrawable instanceof StateListDrawable
                        && bg.getCurrent() instanceof ColorDrawable
                        && lastDrawable.getCurrent() instanceof ColorDrawable) {
                    int start = ((ColorDrawable) lastDrawable.getCurrent()).getColor();
                    int end = ((ColorDrawable) bg.getCurrent()).getColor();
                    if (start != end && attrAnimationList != null) {
                        attrAnimationList
                                .add(new ColorDrawableAttrAnim((ColorDrawable) bg.getCurrent(), start, end));
                    }
                    view.setBackground(bg);
                } else {
                    AnimBackgroundDrawable drawable = new AnimBackgroundDrawable(view.getBackground(), bg);
                    view.setBackground(drawable);
                    if (attrAnimationList != null) {
                        attrAnimationList.add(drawable);
                    }
                }
            } else {
                view.setBackground(bg);
            }
        }
    }
}
