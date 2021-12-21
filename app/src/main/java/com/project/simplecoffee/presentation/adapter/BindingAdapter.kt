package com.project.simplecoffee.presentation.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.simplecoffee.R
import com.project.simplecoffee.presentation.common.ItemVM
import com.project.simplecoffee.utils.common.BitmapCache
import kotlinx.coroutines.DelicateCoroutinesApi

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

@DelicateCoroutinesApi
@BindingAdapter("bitMap")
fun loadImage(imageView: ImageView, url: String?) {
    BitmapCache.setBitMap(imageView, url)
}