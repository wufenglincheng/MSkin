package com.darkliu.mskin.attr;

/**
 * @author liuting
 */
public class AttrInfo {
    /**
     * 属性的名称, ex: background or textSize or textColor
     */
    public String attrName;

    /**
     * 属性值得引用资源ID, normally is [2130745655]
     */
    public int attrValueRefId;

    /**
     * 属性值的名称 , such as [app_exit_btn_background]
     */
    public String attrValueRefName;

    /**
     * 值的类型 , such as color or drawable
     */
    public String attrValueTypeName;

}
