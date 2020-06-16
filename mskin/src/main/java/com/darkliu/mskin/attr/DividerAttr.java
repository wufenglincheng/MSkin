package com.darkliu.mskin.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;

import com.darkliu.mskin.IAttrAnim;
import com.darkliu.mskin.ISkinResource;
import com.darkliu.mskin.SkinManager;

import java.util.List;

/**
 * 分割线的属性替换
 *
 * @author liuting
 */
public class DividerAttr extends SkinAttr {

    public int dividerHeight = 1;

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        if (view instanceof ListView) {
            ListView tv = (ListView) view;
            if (ISkinResource.COLOR.equals(attrInfo.attrValueTypeName)) {
                int color = SkinManager.resource().getColor(view.getContext(), attrInfo.attrValueRefId);
                ColorDrawable sage = new ColorDrawable(color);
                tv.setDivider(sage);
                tv.setDividerHeight(dividerHeight);
            } else if (ISkinResource.DRAWABLE.equals(attrInfo.attrValueTypeName)) {
                tv.setDivider(SkinManager.resource().getDrawable(view.getContext(), attrInfo.attrValueRefId));
            }
        }
    }
}
