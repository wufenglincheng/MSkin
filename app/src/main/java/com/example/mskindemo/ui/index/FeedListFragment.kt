package com.example.mskindemo.ui.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mskindemo.R
import com.example.mskindemo.entry.ItemData
import kotlinx.android.synthetic.main.fragment_feedlist.*

/**
 * Created by liuting on 18/8/2.
 */
var recyclerPool: RecyclerView.RecycledViewPool? = null

class FeedListFragment : Fragment(), (List<ItemData>) -> Unit {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_feedlist, container, false)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (recyclerPool == null) {
      recyclerPool = listView.recycledViewPool
    } else {
      listView.setRecycledViewPool(recyclerPool)
    }
    listView.layoutManager = LinearLayoutManager(context)
    listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
          RecyclerView.SCROLL_STATE_IDLE -> {
            Glide.with(context!!).resumeRequestsRecursive()
          }
          RecyclerView.SCROLL_STATE_DRAGGING -> {
          }
          RecyclerView.SCROLL_STATE_SETTLING -> {
            Glide.with(context!!).pauseRequests()
          }
        }
      }
    })
    DataProvide.register(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    DataProvide.unRegister(this)
  }

  override fun invoke(data: List<ItemData>) {
    listView.adapter = TimeLineAdapter(data, context!!)
  }
}