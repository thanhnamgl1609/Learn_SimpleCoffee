package com.project.simplecoffee.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.DrinkCategory
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val container: MainContainer,
    private val drinkRepo: IDrinkRepo,
    private val drinkCategoryRepo: IDrinkCategoryRepo,
) : ViewModel() {
    val listDrinkCategory = MutableLiveData<List<DrinkCategoryItemVM>>()
    val listDrink = MutableLiveData<List<DrinkItemVM>>()

    private var listAllDrinkVM = mutableListOf<DrinkItemVM>()

    init {
        loadDrink()
        loadDrinkCategory(this)
    }

    private fun loadDrinkCategory(
        menuVM: MenuVM
    ) = viewModelScope.launch {
        val listDrinkCategoryVM = mutableListOf<DrinkCategoryItemVM>()
        when (val result = drinkCategoryRepo.getDrinkCategory()) {
            is Resource.OnSuccess -> {
                result.data?.forEach { drinkCategory ->
                    listDrinkCategoryVM.add(
                        DrinkCategoryItemVM(
                            menuVM,
                            drinkCategory
                        )
                    )
                    listDrinkCategory.value = listDrinkCategoryVM
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun loadDrink() = viewModelScope.launch {
        when (val result = drinkRepo.getDrink()) {
            is Resource.OnSuccess -> {
                result.data?.forEach { drink ->
                    listAllDrinkVM.add(DrinkItemVM(drink))
                    listDrink.value = listAllDrinkVM
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun loadDrinkByCategory(
        drinkCategory: DrinkCategory
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (drinkCategory.name == "All") {
            listDrink.postValue(listAllDrinkVM)
        } else {
            val filterDrink = listAllDrinkVM.filter { item ->
                item.drink.category!!.contains(drinkCategory.id!!)
            }.sortedBy { item -> item.drink.name }
            listDrink.postValue(filterDrink)
        }
    }
}