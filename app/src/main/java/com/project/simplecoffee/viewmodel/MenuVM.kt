package com.project.simplecoffee.viewmodel

import androidx.appcompat.widget.SearchView
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
    private var listCurrentDrink = emptyList<DrinkItemVM>()

    val searchViewListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            filter(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            filter(newText)
            return true
        }

        private fun filter(query: String?) =
            viewModelScope.launch(Dispatchers.IO) {
                listCurrentDrink = if (query != null && query.isNotEmpty()) {
                    listCurrentDrink.filter { item ->
                        item.drink.name!!.contains(query, ignoreCase = true)
                    }
                } else
                    listAllDrinkVM
                listDrink.postValue(listCurrentDrink)
            }
    }

    init {
        loadDrink()
        loadDrinkCategory(this)
    }

    private fun loadDrinkCategory(
        menuVM: MenuVM
    ) = viewModelScope.launch(Dispatchers.IO) {
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
                    listDrinkCategory.postValue(listDrinkCategoryVM)
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun loadDrink() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = drinkRepo.getDrink()) {
            is Resource.OnSuccess -> {
                result.data?.forEach { drink ->
                    listAllDrinkVM.add(DrinkItemVM(drink))
                }
                listDrink.postValue(listAllDrinkVM)
                listCurrentDrink = listAllDrinkVM
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
            listCurrentDrink = listAllDrinkVM
        } else {
            listCurrentDrink = listAllDrinkVM.filter { item ->
                item.drink.category!!.contains(drinkCategory.id!!)
            }.sortedBy { item -> item.drink.name }
            listDrink.postValue(listCurrentDrink)
        }
    }
}