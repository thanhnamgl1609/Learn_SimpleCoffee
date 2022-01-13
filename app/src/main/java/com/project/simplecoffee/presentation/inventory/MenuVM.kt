package com.project.simplecoffee.presentation.inventory

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.*
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.domain.model.details.Role
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetAllDrinkUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetDrinkByCategoryUseCase
import com.project.simplecoffee.domain.usecase.inventory.SearchDrinkUseCase
import com.project.simplecoffee.domain.usecase.inventory.GetAllDrinkCategoryUseCase
import com.project.simplecoffee.domain.usecase.order.AddDrinkToCartUseCase
import com.project.simplecoffee.domain.usecase.user.GetCartUserUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllDrinkUseCase: GetAllDrinkUseCase,
    private val getDrinkByCategoryUseCase: GetDrinkByCategoryUseCase,
    private val searchDrinkUseCase: SearchDrinkUseCase,
    private val getAllDrinkCategoryUseCase: GetAllDrinkCategoryUseCase,
    private val getCartUseCase: GetCartUserUseCase,
    private val addDrinkToCartUseCase: AddDrinkToCartUseCase
) : ViewModel() {
    private val _listDrinkCategory = MutableLiveData<List<DrinkCategoryItemVM>>()
    private val _listDrink = MutableLiveData<List<DrinkItemVM>>()
    private val _greeting = MutableLiveData("Welcome to Simple Coffee!")
    private val _userURL = MutableLiveData(User.AVATAR_DEFAULT)
    private val _drinkAmount = MutableLiveData("0")

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
            progressBarVisibility.value = View.GONE
        }
    }

    init {
        loadData()
    }

    fun loadData() {
        checkLogInStatus()
        loadDrink()
        loadDrinkCategory()
    }

    private fun loadTotalCart() = viewModelScope.launch {
        handleCartResult(getCartUseCase())
    }

    private fun loadDrinkCategory() = viewModelScope.launch {
        handleDrinkCategoryResult(getAllDrinkCategoryUseCase())
    }

    private fun loadDrink() = viewModelScope.launch {
        progressBarVisibility.value = View.VISIBLE
        handleDrinkResult(getAllDrinkUseCase())
        progressBarVisibility.value = View.GONE
    }

    fun loadDrinkByCategory(
        drinkCategory: DrinkCategory
    ) = viewModelScope.launch {
        progressBarVisibility.value = View.VISIBLE
        handleDrinkResult(getDrinkByCategoryUseCase(drinkCategory.id))
        progressBarVisibility.value = View.GONE
    }

    private fun handleDrinkResult(result: Resource<List<Drink>?>) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        when (result) {
            is Resource.OnSuccess -> {
                val listAllDrinkVM = mutableListOf<DrinkItemVM>()
                result.data?.forEach { drink ->
                    listAllDrinkVM.add(DrinkItemVM(this@MenuVM, drink))
                }
                _listDrink.postValue(listAllDrinkVM)
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    private fun handleDrinkCategoryResult(result: Resource<List<DrinkCategory>?>) =
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            when (result) {
                is Resource.OnSuccess -> {
                    val listAllDrinkCategoryVM = mutableListOf<DrinkCategoryItemVM>()
                    result.data?.forEach { drinkCategory ->
                        listAllDrinkCategoryVM.add(
                            DrinkCategoryItemVM(
                                this@MenuVM,
                                drinkCategory
                            )
                        )
                    }
                    _listDrinkCategory.postValue(listAllDrinkCategoryVM)
                }
                is Resource.OnFailure -> {
                    container.showMessage(result.message.toString())
                }
            }
        }

    fun checkLogInStatus() = viewModelScope.launch {
        getCurrentUserUseCase()?.apply {
            loadTotalCart()
            _greeting.value = "Hello, $firstname $lastname"
            _userURL.value = avatar
        }
    }

    fun addToCart(drink: Drink) = viewModelScope.launch {
        if (getCurrentUserUseCase() == null) {
            container.onSignIn()
        } else {
            handleCartResult(addDrinkToCartUseCase(drink))
        }
    }

    private fun handleCartResult(result: Resource<Cart?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val amount = result.data!!.items?.sumOf { orderItem -> orderItem.quantity } ?: 0
                _drinkAmount.postValue(
                    amount.toString()
                )
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun onCartClick() = viewModelScope.launch {
        getCurrentUserUseCase()?.apply {
            val frag = if (role is Role.Customer)
                AllMainFragment.CartCustomer
            else
                AllMainFragment.CartStaff
            container.loadFragment(frag.createFragment(), true)
        } ?: run {
            container.onSignIn()
        }
    }
}