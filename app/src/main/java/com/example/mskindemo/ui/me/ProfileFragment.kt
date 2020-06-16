package com.example.mskindemo.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkliu.mskin.SkinManager
import com.example.mskindemo.R
import com.example.mskindemo.base.BaseSkinFragment
import com.example.mskindemo.*
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by liuting on 18/8/2.
 */
class ProfileFragment : BaseSkinFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return SkinManager.resource().getLayout(container?.context, R.layout.fragment_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    text.setSkinBackgroundColor(R.color.skin_background)
    text.setSkinTextColor(R.color.skin_textColor)
    image.setSkinImageDrawable(R.drawable.ic_home)
  }

  override fun onThemeChange() {
  }
}