package com.darkliu.mskin.attr;

/**
 * @author liuting
 */
public class AttrFactory {
    private static final String BACKGROUND = "background";
    private static final String TEXT_COLOR = "textColor";
    private static final String TEXT_COLOR_HINT = "textColorHint";
    private static final String LIST_SELECTOR = "listSelector";
    private static final String DIVIDER = "divider";
    private static final String SRC = "src";
    private static final String DRAWABLE_BOTTOM = "drawableBottom";
    private static final String DRAWABLE_LEFT = "drawableLeft";
    private static final String DRAWABLE_RIGHT = "drawableRight";
    private static final String DRAWABLE_TOP = "drawableTop";
    private static final String PROGRESS_DRAWABLE = "progressDrawable";

    /**
     * 是否是支持的属性
     *
     * @param attrName
     * @return
     */
    public static boolean isSupportedAttr(String attrName) {
        switch (attrName) {
            case BACKGROUND:
            case TEXT_COLOR:
            case TEXT_COLOR_HINT:
            case LIST_SELECTOR:
            case DIVIDER:
            case SRC:
            case DRAWABLE_BOTTOM:
            case DRAWABLE_LEFT:
            case DRAWABLE_RIGHT:
            case DRAWABLE_TOP:
            case PROGRESS_DRAWABLE:
                return true;
        }
        return false;
    }

    /**
     * @param attrName  属性名称（e.g. android:[background]）
     * @param id        属性的值 (e.g. 1231231)
     * @param entryName 值的名称 (e.g. R.drawable.[xxx])
     * @param typeName  值的类型，像background的值有color和drawable俩种类型
     * @return
     */
    public static SkinAttr get(String attrName, int id, String entryName, String typeName) {
        SkinAttr attr = null;
        if (BACKGROUND.equals(attrName)) {
            attr = new BackgroundAttr();
        } else if (TEXT_COLOR.equals(attrName)) {
            attr = new TextColorAttr();
        } else if (TEXT_COLOR_HINT.equals(attrName)) {
            attr = new TextColorHintAttr();
        } else if (LIST_SELECTOR.equals(attrName)) {
            attr = new ListSelectorAttr();
        } else if (DIVIDER.equals(attrName)) {
            attr = new DividerAttr();
        } else if (SRC.equals(attrName)) {
            attr = new SrcAttr();
        } else if (DRAWABLE_LEFT.equals(attrName)) {
            attr = new DrawableXAttr(0);
        } else if (DRAWABLE_TOP.equals(attrName)) {
            attr = new DrawableXAttr(1);
        } else if (DRAWABLE_RIGHT.equals(attrName)) {
            attr = new DrawableXAttr(2);
        } else if (DRAWABLE_BOTTOM.equals(attrName)) {
            attr = new DrawableXAttr(3);
        } else if (PROGRESS_DRAWABLE.equals(attrName)) {
            attr = new ProgressDrawableAttr();
        }

        if (attr != null) {
            AttrInfo info = new AttrInfo();
            info.attrName = attrName;
            info.attrValueRefId = id;
            info.attrValueTypeName = typeName;
            info.attrValueRefName = entryName;
            attr.setAttrInfo(info);
        }
        return attr;
    }
}
