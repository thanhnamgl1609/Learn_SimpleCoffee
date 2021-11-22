package com.project.simplecoffee.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.simplecoffee.viewmodel.ItemVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.concurrent.Executors
import java.util.logging.Handler

@BindingAdapter("itemList")
fun updateItem(recyclerView: RecyclerView, listItems: List<ItemVM>?) {
    listItems?.let {
        getAdapter(recyclerView).updateItemList(it)
    }
}

private fun getAdapter(recyclerView: RecyclerView): MainRecyclerAdapter {
    if (recyclerView.adapter == null || recyclerView.adapter !is MainRecyclerAdapter) {
        recyclerView.adapter = MainRecyclerAdapter()
    }
    return recyclerView.adapter as MainRecyclerAdapter
}

@BindingAdapter("bitMap")
fun loadImage(imageView: ImageView, bitmap: Bitmap?) {
    imageView.setImageBitmap(bitmap)
}
