package com.example.mskindemo.ui.index


import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mskindemo.R
import com.example.mskindemo.view.ItemDecorationAlbumColumns
import com.example.mskindemo.MyApplication
import com.example.mskindemo.entry.ItemData
import java.util.ArrayList

/**
 * Created by liuting on 17/6/20.
 */

class TimeLineAdapter constructor(val datas: List<ItemData>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = when (viewType) {
      ItemData.VIEW_TYPE_SINGLE ->
        inflater.inflate(R.layout.item_single_image, parent, false)
      ItemData.VIEW_TYPE_MUTIL ->
        inflater.inflate(R.layout.item_single_images, parent, false)
      else ->
        inflater.inflate(R.layout.item_single_image, parent, false)
    }
    return ViewHolder(view)
  }

  override fun getItemViewType(position: Int) = datas[position].viewType
  override fun getItemCount() = datas.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = datas[position]
    val content = holder.get<TextView>(R.id.item_content)
    content.text = data.text
    val imageParentWidth = MyApplication.screenWidth() - MyApplication.dpToPx(90)
    when (getItemViewType(position)) {
      ItemData.VIEW_TYPE_SINGLE -> buildSingleImage(data, holder, imageParentWidth)
      ItemData.VIEW_TYPE_MUTIL -> buildMutilImages(data, holder, imageParentWidth)
    }
  }

  private fun buildMutilImages(data: ItemData, holder: ViewHolder, imageParentWidth: Int) {
    val recyclerView = holder.get<RecyclerView>(R.id.item_image)
    val column = if (data.images.size === 4) 2 else 3
    recyclerView.layoutManager = GridLayoutManager(context, column)
    val space = MyApplication.dpToPx(3)
    val itemDecoration = ItemDecorationAlbumColumns(space, column)
    val itemWidth = (imageParentWidth - space * (column - 1)) / column
    val itemHeight = if (column == 2) (itemWidth * 9f / 16).toInt() else itemWidth
    clearItemDecorations(recyclerView)
    recyclerView.addItemDecoration(itemDecoration)
    recyclerView.isFocusable = false
    recyclerView.adapter = object : RecyclerView.Adapter<ViewHolder>() {
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val image = ImageView(parent.context)
        val layoutParams = ViewGroup.LayoutParams(itemWidth, itemHeight)
        image.layoutParams = layoutParams
        return ViewHolder(image)
      }

      override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = holder.itemView as ImageView
        Glide.with(context)
          .load(data.images[position])
          .apply(RequestOptions().centerCrop())
          .into(image)
      }

      override fun getItemCount(): Int {
        return data.images.size
      }
    }
  }

  fun clearItemDecorations(recyclerView: RecyclerView) {
    try {
      val field = RecyclerView::class.java.getDeclaredField("mItemDecorations")
      field.isAccessible = true
      val itemDecorations = field.get(recyclerView) as ArrayList<RecyclerView.ItemDecoration>
      itemDecorations.clear()
    } catch (e: Throwable) {
      e.printStackTrace()
    }

  }

  private fun buildSingleImage(data: ItemData, holder: ViewHolder, imageParentWidth: Int) {
    val imageView = holder.get<ImageView>(R.id.item_image)
    imageView.layoutParams.height = (imageParentWidth * 9f / 16).toInt()
    imageView.layoutParams = imageView.layoutParams
    Glide.with(context)
      .load(data.images[0])
      .apply(RequestOptions().centerCrop())
      .into(imageView)
    imageView.setBackgroundColor(Color.WHITE)
  }

}

class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

  private val views = SparseArray<View>()

  fun <T : View> get(id: Int): T {
    var view: View? = views.get(id)
    if (view == null) {
      view = itemView.findViewById(id)
    }
    return view as T
  }
}
