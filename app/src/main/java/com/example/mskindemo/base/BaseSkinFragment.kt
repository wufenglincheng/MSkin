package com.example.mskindemo.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.darkliu.mskin.IThemeUpdate
import com.example.mskindemo.dp2px

/**
 * Created by liuting on 18/8/3.
 */
abstract class BaseSkinFragment : Fragment(), IThemeUpdate {

    override fun onThemeUpdate(isChange: Boolean) {
        onThemeChange()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onThemeChange()
      dp2px(2)
    }

    abstract fun onThemeChange()
}