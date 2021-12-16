package com.project.simplecoffee.presentation.inventory

import androidx.lifecycle.*
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.DrinkCategory
import com.project.simplecoffee.presentation.common.ItemVM
import kotlinx.coroutines.launch


class DrinkCategoryItemVM(
    private val menuVM: MenuVM,
    private val item: DrinkCategory
) : ViewModel(), ItemVM {
    override val viewType: Int = R.layout.category_item

    val name = MutableLiveData<String>().apply { postValue(item.name) }

    fun onClick() =
        viewModelScope.launch {
            menuVM.loadDrinkByCategory(item)
        }
}