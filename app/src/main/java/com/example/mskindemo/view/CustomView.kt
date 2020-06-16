package com.example.mskindemo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.mskindemo.R
import com.example.mskindemo.opt

class CustomView : View {

  private var mDrawable: Drawable? = null

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
  constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
    val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomView)
    opt(typeArray, R.styleable.CustomView_bgColor, Color.BLACK, ::setBackgroundColor)
    opt(typeArray, R.styleable.CustomView_leftDrawable, ::setDrawable)
  }

  private fun setDrawable(drawable: Drawable?) {
    mDrawable = drawable
    mDrawable?.setBounds(0, 0, mDrawable?.intrinsicWidth ?: 0, mDrawable?.intrinsicHeight ?: 0)
    invalidate()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mDrawable?.draw(canvas)
  }
}