package com.example.mskindemo

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleableRes
import com.darkliu.mskin.SkinManager

fun View.opt(array: TypedArray, @StyleableRes index: Int, @ColorInt defColor: Int, f: (Int) -> Unit) {
  SkinManager.applySkinColor(this, array, index, defColor, f)
}

fun View.opt(array: TypedArray, @StyleableRes index: Int, f: (Drawable) -> Unit) {
  SkinManager.applySkinDrawable(this, array, index, f)
}

fun View.setSkinBackgroundColor(@ColorRes color: Int) {
  SkinManager.applySkinColor(this, color, ::setBackgroundColor)
}

fun TextView.setSkinTextColor(@ColorRes color: Int) {
  SkinManager.applySkinColor(this, color, ::setTextColor)
}

fun ImageView.setSkinImageDrawable(@DrawableRes drawable: Int) {
  SkinManager.applySkinDrawable(this, drawable, ::setImageDrawable)
}