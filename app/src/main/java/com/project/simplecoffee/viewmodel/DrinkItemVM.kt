package com.project.simplecoffee.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.models.Drink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class DrinkItemVM(
    val drink: Drink
) : ViewModel(), ItemVM {
    override val viewType: Int = R.layout.drink_row_item

    val name = MutableLiveData<String>(drink.name)
    val price = MutableLiveData(drink.price.toString())
    val bitmap = MutableLiveData<Bitmap>(drink.bitmap)
}