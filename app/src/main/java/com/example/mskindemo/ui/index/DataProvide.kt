package com.example.mskindemo.ui.index

import android.content.Context
import com.example.mskindemo.entry.ItemData
import com.example.mskindemo.forEach
import com.example.mskindemo.runOnMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by liuting on 18/8/2.
 */
object DataProvide {

  private val dataList: MutableList<ItemData> = ArrayList()
  private var isFinish: Boolean = false
  private val dataListener: MutableList<(List<ItemData>) -> Unit> = ArrayList()
  fun init(context: Context) = GlobalScope.launch(Dispatchers.IO) {
    val result = loadData(context)
    runOnMain {
      dataList.addAll(result)
      for (listener in dataListener) {
        listener(dataList)
      }
      isFinish = true
    }
  }

  fun register(listener: (List<ItemData>) -> Unit) {
    if (isFinish) {
      listener(dataList)
    } else {
      dataListener.add(listener)
    }
  }

  fun unRegister(listener: (List<ItemData>) -> Unit) {
    dataListener.remove(listener)
  }

  private fun loadData(context: Context): List<ItemData> {
    val arrayList = ArrayList<ItemData>()
    val response = getDataJson(context)

    JSONArray(response).forEach<JSONObject> {
      val itemData = ItemData()
      itemData.text = it.optString("text")
      val imageList: MutableList<String> = ArrayList()
      it.optJSONArray("images")?.forEach<String> { string ->
        imageList.add(string)
      }
      itemData.images = imageList
      arrayList.add(itemData)
    }
    return arrayList
  }


  private fun getDataJson(context: Context): String {
    try {
      val inputReader = InputStreamReader(context.resources.assets.open("data.json"))
      return BufferedReader(inputReader).readText()
    } catch (e: Exception) {
    }

    return ""
  }
}