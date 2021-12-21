package com.project.simplecoffee.presentation.order

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.presentation.common.ItemVM
import com.project.simplecoffee.presentation.common.main.MainContainer

class TableItemVM constructor(
    private val container: MainContainer,
    private val tableStatusVM: TableStatusVM,
    private val table: Table
) : ItemVM {
    override val viewType = R.layout.table_item
    private val _numberOrder = MutableLiveData((table.no?: "").toString())
    private val _url = MutableLiveData(table.image ?: "")
    private val _color = MutableLiveData<Drawable>().apply {
        value = if (table.order == null) // Available
            container.loadDrawable(R.drawable.available_table)
        else
            container.loadDrawable(R.drawable.unavailable_table)
    }

    val numberOrder: LiveData<String>
        get() = _numberOrder
    val url: LiveData<String>
        get() = _url
    val color: LiveData<Drawable>
        get() = _color
}