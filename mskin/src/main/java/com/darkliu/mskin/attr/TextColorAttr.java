package com.darkliu.mskin.attr;

import android.view.View;
import android.widget.TextView;


import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;

/**
 * @author liuting
 */
public class TextColorAttr extends SkinAttr {

    @Override
    public void apply(View tv, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (tv instanceof TextView) {
            if (ISkinResource.COLOR.equals(attrInfo.attrValueTypeName)) {
                ((TextView) tv).setTextColor(SkinManager.resource().getColor(tv.getContext(), attrInfo.attrValueRefId));
            }
        }
    }

}
