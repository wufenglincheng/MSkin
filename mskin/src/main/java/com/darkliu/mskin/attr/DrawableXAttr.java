package com.darkliu.mskin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;

/**
 * TextView 的Drawable的设置
 *
 * @author liuting
 */
public class DrawableXAttr extends SkinAttr {
    private final int drawableIndex;

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (view instanceof TextView) {
            if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName)) {
                Drawable bg = SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId);
                // 得到之前的drawables，对其进行更新
                Drawable[] drawables = ((TextView) view).getCompoundDrawables();
                drawables[drawableIndex] = bg;
                // 重新设置进去
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1],
                        drawables[2], drawables[3]);
                ((TextView) view).setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                        drawables[3]);
            }
        }
    }

    public DrawableXAttr(int drawableIndex) {
        this.drawableIndex = drawableIndex;
    }

}
