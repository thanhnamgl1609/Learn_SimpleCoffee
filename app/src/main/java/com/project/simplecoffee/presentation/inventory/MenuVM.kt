package com.project.simplecoffee.presentation.inventory

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.Drink
import com.project.simplecoffee.domain.model.DrinkCategory
import com.project.simplecoffee.domain.model.UserInfo
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetAllDrinkUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetDrinkByCategoryUseCase
import com.project.simplecoffee.domain.usecase.inventory.SearchDrinkUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetAllDrinkCategoryUseCase
import com.project.simplecoffee.domain.usecase.user.GetCurrentUserInfoUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val getAllDrinkUseCase: GetAllDrinkUseCase,
    private val getDrinkByCategoryUseCase: GetDrinkByCategoryUseCase,
    private val searchDrinkUseCase: SearchDrinkUseCase,
    private val getAllDrinkCategoryUseCase: GetAllDrinkCategoryUseCase
) : ViewModel() {
    private val _listDrinkCategory = MutableLiveData<List<DrinkCategoryItemVM>>()
    private val _listDrink = MutableLiveData<List<DrinkItemVM>>()
    private val _greeting = MutableLiveData("Welcome to Simple Coffee!")
    private val _userURL = MutableLiveData(UserInfo.AVATAR_DEFAULT)
    private val _drinkAmount = MutableLiveData("1")

    val listDrinkCategory: LiveData<List<DrinkCategoryItemVM>>
        get() = _listDrinkCategory
    val listDrink: LiveData<List<DrinkItemVM>>
        get() = _listDrink
    val greeting: LiveData<String>
        get() = _greeting
    val userURL: LiveData<String>
        get() = _userURL
    val drinkAmount: LiveData<String>
        get() = _drinkAmount
    val progressBarVisibility = MutableLiveData(View.VISIBLE)

    val searchViewListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            filter(query)
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            filter(query)
            return true
        }

        fun filter(query: String?) = viewModelScope.launch {
            progressBarVisibility.postValue(View.VISIBLE)
            val result = viewModelScope.async { searchDrinkUseCase(query) }
            handleDrinkResult(result.await())
        }
    }

    init {
        loadDrink()
        loadDrinkCategory()
    }

    private fun loadDrinkCategory() = viewModelScope.launch {
        handleDrinkCategoryResult(getAllDrinkCategoryUseCase())
    }

    private fun loadDrink() = viewModelScope.launch {
        progressBarVisibility.postValue(View.VISIBLE)
        handleDrinkResult(getAllDrinkUseCase())
    }

    fun loadDrinkByCategory(
        drinkCategory: DrinkCategory
    ) = viewModelScope.launch {
        progressBarVisibility.postValue(View.VISIBLE)
        handleDrinkResult(getDrinkByCategoryUseCase(drinkCategory.id))
    }

    private fun handleDrinkResult(result: Resource<List<Drink>?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val listAllDrinkVM = mutableListOf<DrinkItemVM>()
                result.data?.forEach { drink ->
                    listAllDrinkVM.add(DrinkItemVM(this, drink))
                }
                _listDrink.value = listAllDrinkVM
                progressBarVisibility.postValue(View.GONE)
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun handleDrinkCategoryResult(result: Resource<List<DrinkCategory>?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val listAllDrinkCategoryVM = mutableListOf<DrinkCategoryItemVM>()
                result.data?.forEach { drinkCategory ->
                    listAllDrinkCategoryVM.add(DrinkCategoryItemVM(this, drinkCategory))
                }
                _listDrinkCategory.value = listAllDrinkCategoryVM
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun checkLogInStatus() = viewModelScope.launch {
        val userInfo = getCurrentUserInfoUseCase().data
        userInfo?.apply {
            _greeting.value = "Hello, $firstname $lastname"
            _userURL.value = avatar
        }
    }

    fun addToCart(drink: Drink) = viewModelScope.launch {
        if (getCurrentUserUseCase() == null) {
            container.onSignIn()
        } else {
            container.showMessage("On add to cart")
        }
    }

    fun onCartClick() = viewModelScope.launch {
        if (getCurrentUserUseCase() == null) {
            container.onSignIn()
        } else {
            container.showMessage("On cart click")
        }
    }
}