package com.example.mskindemo

import android.app.Application
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.appcompat.app.AppCompatDelegate
import com.darkliu.mskin.SkinManager
import com.example.mskindemo.ui.index.DataProvide
import org.json.JSONArray

/**
 * Created by liuting on 18/8/2.
 */
class MyApplication : Application() {
  companion object {
    lateinit var mDisplayMetrics: DisplayMetrics
    val THEME_NIGHT: String = Environment.getExternalStorageDirectory().absolutePath + "/1/night.skin"
    val THEME_ONE: String = Environment.getExternalStorageDirectory().absolutePath + "/1/one.skin"
    val THEME_TWO: String = Environment.getExternalStorageDirectory().absolutePath + "/1/two.skin"
    val THEME_THREE: String = Environment.getExternalStorageDirectory().absolutePath + "/1/three.skin"

    fun screenWidth() = mDisplayMetrics.widthPixels
    fun screenHeight() = mDisplayMetrics.heightPixels
    fun dpToPx(dp: Int): Int {
      return (mDisplayMetrics.density * dp).toInt()
    }

  }

  override fun onCreate() {
    super.onCreate()
    mDisplayMetrics = resources.displayMetrics
    SkinManager.instance().init(this)
    DataProvide.init(this)
    AppCompatDelegate.setDefaultNightMode(
      AppCompatDelegate.MODE_NIGHT_YES)
  }
}

fun dp2px(dp: Int): Int {
  return MyApplication.dpToPx(dp)
}

fun runOnMain(r: () -> Unit) {
  Handler(Looper.getMainLooper()).post(r)
}

fun <T> JSONArray.forEach(action: (T) -> Unit) {
  for (i in 0 until length()) {
    val any = opt(i)
    val result = any as? T
    if (result != null) {
      action(result)
    }
  }
}