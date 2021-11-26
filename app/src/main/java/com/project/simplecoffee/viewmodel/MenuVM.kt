package com.project.simplecoffee.viewmodel

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.common.Resource
import com.project.simplecoffee.domain.models.Drink
import com.project.simplecoffee.domain.models.DrinkCategory
import com.project.simplecoffee.domain.repository.IDrinkCategoryRepo
import com.project.simplecoffee.domain.repository.IDrinkRepo
import com.project.simplecoffee.domain.repository.IUserRepo
import com.project.simplecoffee.views.main.MainContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val container: MainContainer,
    private val userRepo: IUserRepo,
    private val drinkRepo: IDrinkRepo,
    private val drinkCategoryRepo: IDrinkCategoryRepo,
) : ViewModel() {
    private val _listDrinkCategory = MutableLiveData<List<DrinkCategoryItemVM>>()
    private val _listDrink = MutableLiveData<List<DrinkItemVM>>()
    private val _greeting = MutableLiveData("Welcome to Simple Coffee!")
    private val _drinkAmount = MutableLiveData("1")
    private var listAllDrinkVM = mutableListOf<DrinkItemVM>()
    private var listCurrentDrink = emptyList<DrinkItemVM>()

    val listDrinkCategory: LiveData<List<DrinkCategoryItemVM>>
        get() = _listDrinkCategory
    val listDrink: LiveData<List<DrinkItemVM>>
        get() = _listDrink
    val greeting: LiveData<String>
        get() = _greeting
    val drinkAmount: LiveData<String>
        get() = _drinkAmount
    val progressBarVisibility = MutableLiveData(View.VISIBLE)

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
                _listDrink.postValue(listCurrentDrink)
            }
    }

    init {
        loadDrink(this)
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
                    _listDrinkCategory.postValue(listDrinkCategoryVM)
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun loadDrink(menuVM: MenuVM) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = drinkRepo.getAllDrink()) {
            is Resource.OnSuccess -> {
                result.data?.forEach { drink ->
                    listAllDrinkVM.add(DrinkItemVM(menuVM, drink))
                }
                _listDrink.postValue(listAllDrinkVM)
                listCurrentDrink = listAllDrinkVM
                progressBarVisibility.postValue(View.GONE)
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
            _listDrink.postValue(listAllDrinkVM)
            listCurrentDrink = listAllDrinkVM
        } else {
            listCurrentDrink = listAllDrinkVM.filter { item ->
                item.drink.category!!.contains(drinkCategory.id!!)
            }.sortedBy { item -> item.drink.name }
            _listDrink.postValue(listCurrentDrink)
        }
    }

    fun checkLogInStatus() = viewModelScope.launch {
        if (userRepo.getCurrentUser() != null) {
            val userInfoRepo = userRepo.getUserInfoRepo().data
            val userInfo = userInfoRepo?.getUserInfo()?.data
            userInfo?.apply {
                _greeting.postValue("Hello, $firstname $lastname")
            }
        } else {
            _greeting.postValue("Welcome to Simple Coffee!")
        }
    }

    fun addToCart(drink: Drink) {
        if (userRepo.getCurrentUser() == null) {
            container.onSignIn()
        } else {
            container.showMessage("On add to cart")
        }
    }

    fun onCartClick() {
        if (userRepo.getCurrentUser() == null) {
            container.onSignIn()
        } else {
            container.showMessage("On cart click")
        }
    }
}