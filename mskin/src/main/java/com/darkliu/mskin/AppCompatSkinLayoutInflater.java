package com.darkliu.mskin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author liuting
 */
public class AppCompatSkinLayoutInflater extends BaseSkinLayoutInflater {

  private static Method method;
  private final AppCompatDelegate delegate;


  AppCompatSkinLayoutInflater(AppCompatDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
    View view = null;
    try {
      /**
       * 先调用AppCompatDelegate中的Factory2{@link #AppCompatDelegateImplV9}的方法
       **/
      if (method == null) {
        method = delegate.getClass().getMethod("createView", View.class, String.class,
            Context.class, AttributeSet.class);
      }
      view = (View) method.invoke(delegate, parent, name, context, attrs);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
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
    return null;
  }
}
