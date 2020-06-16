package com.example.mskindemo.ui.discovery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.mskindemo.R
import com.example.mskindemo.base.BaseSkinFragment


/**
 * Created by liuting on 18/8/2.
 */
class DiscoveryFragment : BaseSkinFragment() {
  override fun onThemeChange() {

  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_discovery, container, false)
  }
}