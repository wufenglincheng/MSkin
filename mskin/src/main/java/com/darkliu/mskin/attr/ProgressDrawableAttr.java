package com.darkliu.mskin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;


/**
 * @author liuting
 */
public class ProgressDrawableAttr extends SkinAttr {
    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName) && view instanceof ProgressBar) {
            Drawable drawable = SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId);
            ((ProgressBar) view).setProgressDrawable(drawable);
        }
    }
}
