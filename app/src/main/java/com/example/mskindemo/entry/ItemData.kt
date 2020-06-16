package com.example.mskindemo.entry

import java.util.ArrayList

class ItemData {
  var images: List<String> = ArrayList()
  var text: String? = null
  var isLike: Boolean = false
  var comboCount: Int = 0

  val viewType: Int
    get() = if (images.size == 1) {
      VIEW_TYPE_SINGLE
    } else {
      VIEW_TYPE_MUTIL
    }

  companion object {
    val VIEW_TYPE_SINGLE = 0
    val VIEW_TYPE_MUTIL = 1
  }
}