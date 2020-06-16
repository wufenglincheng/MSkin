package com.darkliu.mskin;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.darkliu.mskin.attr.AttrFactory;
import com.darkliu.mskin.attr.SkinAttr;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

/**
 * @author liuting
 */
public abstract class BaseSkinLayoutInflater implements LayoutInflater.Factory2 {
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();

    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private final Object[] mConstructorArgs = new Object[2];


    void parseViewAttr(View view, Context context, AttributeSet attrs) {
        final List<ISkinAttrApply> skinAttrs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (AttrFactory.isSupportedAttr(attrName)) {
                SkinAttr skinAttr = parseDefaultAttr(context, attrValue, attrName);
                if (skinAttr != null) {
                    skinAttrs.add(skinAttr);
                }
            }
        }
        ViewThemeProcessor processor = SkinManager.getProcessor(view);
        processor.addAll(skinAttrs);
    }

    @Nullable
    private SkinAttr parseDefaultAttr(Context context, String attrValue, String attrName) {
        SkinAttr mSkinAttr = null;
        if (attrValue.startsWith("@")) {
            try {
                int id = Integer.parseInt(attrValue.substring(1));
                if (id != 0) {
                    String entryName = context.getResources().getResourceEntryName(id);
                    String typeName = context.getResources().getResourceTypeName(id);
                    mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
                }
            } catch (NumberFormatException | Resources.NotFoundException e) {
                //not print exception,because it waste time
            }
        } else if (attrValue.startsWith("?")) {
            TypedArray a =
                    context.obtainStyledAttributes(0, new int[]{Integer.parseInt(attrValue.substring(1))});
            int id = a.getResourceId(0, 0);
            if (id != 0) {
                String entryName = context.getResources().getResourceEntryName(id);
                String typeName = context.getResources().getResourceTypeName(id);
                mSkinAttr = AttrFactory.get(attrName, id, entryName, typeName);
            }
            a.recycle();
        }
        return mSkinAttr;
    }

    /**
     * 参考 AppCompatViewInflater#createViewFromTag 方法
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    View createViewFromTag(String name, Context context, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createViewByPrefix(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createViewByPrefix(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createViewByPrefix(Context context, String name, String prefix) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

}
