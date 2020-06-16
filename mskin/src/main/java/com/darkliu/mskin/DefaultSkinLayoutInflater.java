package com.darkliu.mskin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

/**
 * @author liuting
 */
public class DefaultSkinLayoutInflater extends BaseSkinLayoutInflater {

    private final FragmentActivity delegate;

    public DefaultSkinLayoutInflater(FragmentActivity delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = delegate.onCreateView(parent, name, context, attrs);
        // 如果view为空，我们自己实例化
        if (view == null) {
            view = createViewFromTag(name, context, attrs);
        }
        if (view != null) {
            parseViewAttr(view, context, attrs);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = delegate.onCreateView(name, context, attrs);
        // 如果view为空，我们自己实例化
        if (view == null) {
            view = createViewFromTag(name, context, attrs);
        }
        if (view != null) {
            parseViewAttr(view, context, attrs);
        }
        return view;
    }
}
