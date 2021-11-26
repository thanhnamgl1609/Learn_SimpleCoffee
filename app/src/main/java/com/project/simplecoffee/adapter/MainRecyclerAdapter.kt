package com.project.simplecoffee.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.project.simplecoffee.BR
import com.project.simplecoffee.viewmodel.ItemVM

class MainRecyclerAdapter : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {
    private var itemViewModels = emptyList<ItemVM>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewModels[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemViewModels[position])
    }

    override fun getItemCount(): Int = itemViewModels.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItemList(listItems: List<ItemVM>) {
        itemViewModels = listItems
        notifyDataSetChanged()
    }


    class ViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemVM: ItemVM) {
            binding.setVariable(BR.itemViewModel, itemVM)
        }
    }
}