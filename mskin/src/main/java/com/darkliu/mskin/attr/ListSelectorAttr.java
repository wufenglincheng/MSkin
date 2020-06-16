package com.darkliu.mskin.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AbsListView;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;

/**
 * @author liuting
 */
public class ListSelectorAttr extends SkinAttr {

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (view instanceof AbsListView) {
            AbsListView tv = (AbsListView) view;
            if (ISkinResource.COLOR.equals(attrInfo.attrValueTypeName)) {
                tv.setSelector(new ColorDrawable(SkinManager.resource().getColor(view.getContext(), attrInfo.attrValueRefId)));
            } else if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName)) {
                tv.setSelector(SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId));
            }
        }
    }
}
