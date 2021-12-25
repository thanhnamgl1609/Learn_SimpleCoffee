package com.project.simplecoffee.presentation.user

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.usecase.auth.GetCurrentUserUseCase
import com.project.simplecoffee.domain.usecase.order.GetAllAvailableTableUseCase
import com.project.simplecoffee.domain.usecase.user.*
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartStaffVM @Inject constructor(
    private val container: MainContainer,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCartUserUseCase: GetCartUserUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val makeOrderUseCase: MakeOrderUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getAllAvailableTablesUseCase: GetAllAvailableTableUseCase,
    private val saveCartUseCase: SaveCartUseCase
) : CartVM(
    container, getCurrentUserUseCase, getCartUserUseCase,
    makeOrderUseCase, clearCartUseCase, saveCartUseCase
) {
    private val _emptyTableVisible = MutableLiveData(View.GONE)
    private var tables = emptyList<Table>()

    val emptyTableVisible: LiveData<Int>
        get() = _emptyTableVisible
    val email = MutableLiveData<String>()
    val selectedTable = MutableLiveData(-1)

    init {
        loadCart()
    }

    override fun updateData() {
        cart?.mail?.run {
            email.value = this
        }
        super.updateData()
    }

    fun onFindEmail() {
        viewModelScope.launch {
            if (checkValidEmail())
                container.showMessage("Valid email")
        }
    }

    private suspend fun checkValidEmail() =
        getUserByEmailUseCase(email.value).data?.run {
            true
        } ?: run {
            container.showMessage("The customer doesn't exist")
            false
        }

    private fun checkValidTable() = (selectedTable.value!! >= 0).apply {
        if (!this)
            container.showMessage("Table is not valid")
    }

    override fun onOrderClick() {
        viewModelScope.launch {
            _loadingOrderVisible.value = View.INVISIBLE
            if ((email.value.isNullOrEmpty() || checkValidEmail()) && checkValidTable()) {
                saveStaffCart()
                super.onOrderClick()
            } else
                _loadingOrderVisible.value = View.VISIBLE
        }
    }

    fun setTableSpinner(context: Context, spinner: Spinner) = viewModelScope.launch {
        when (val result = getAllAvailableTablesUseCase()) {
            is Resource.OnSuccess -> {
                tables = result.data!!
                val tableNames = tables.map { table -> table.name }
                ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    tableNames
                ).also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spinner.adapter = adapter
                }
                cart?.apply {
                    if (tables.isNotEmpty()) {
                        table?.run {
                            selectedTable.value = tableNames.indexOf(table)
                        } ?: apply {
                            table = tableNames.first()
                            selectedTable.value = 0
                        }
                        _emptyTableVisible.value = View.GONE
                    } else {
                        _emptyTableVisible.value = View.VISIBLE
                    }
                }
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    override fun save() {
        saveStaffCart()
        super.save()
    }

    private fun saveStaffCart() {
        cart?.apply {
            table = if (selectedTable.value!! > -1) tables[selectedTable.value!!].id else null
            mail = email.value
        }
    }
}