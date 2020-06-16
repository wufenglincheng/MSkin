package com.darkliu.mskin.attr;

import com.darkliu.mskin.ISkinAttrApply;

/**
 * 属性对象
 *
 * @author liuting
 */
public abstract class SkinAttr implements ISkinAttrApply {

    protected AttrInfo attrInfo;

    void setAttrInfo(AttrInfo attrInfo) {
        this.attrInfo = attrInfo;
    }

    @Override
    public String toString() {
        return "SkinAttr \n[\nattrName=" + attrInfo.attrName + ", \n"
                + "attrValueRefId=" + attrInfo.attrValueRefId + ", \n"
                + "attrValueRefName=" + attrInfo.attrValueRefName + ", \n"
                + "attrValueTypeName=" + attrInfo.attrValueTypeName
                + "\n]";
    }
}
