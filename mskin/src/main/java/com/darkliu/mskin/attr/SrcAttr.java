package com.darkliu.mskin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;

/**
 * @author liuting
 */
public class SrcAttr extends SkinAttr {

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (view instanceof ImageView) {
            if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName)) {
                Drawable bg = SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId);
                ((ImageView) view).setImageDrawable(bg);
            }
        }
    }
}
