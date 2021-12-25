package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.usecase.order.GetAllTableUseCase
import com.project.simplecoffee.presentation.common.main.AllMainFragment
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TableStatusVM @Inject constructor(
    private val container: MainContainer,
    private val getAllTableUseCase: GetAllTableUseCase
) : ViewModel() {
    private val _liveListTableItemVM = MutableLiveData<List<TableItemVM>>(emptyList())
    private val _loadingVisible = MutableLiveData(View.GONE)

    val liveListTableItemVM: LiveData<List<TableItemVM>>
        get() = _liveListTableItemVM
    val loadingVisible: LiveData<Int>
        get() = _loadingVisible

    fun getAllTable() = viewModelScope.launch {
        _loadingVisible.value = View.VISIBLE
        handleResult(getAllTableUseCase())
        _loadingVisible.value = View.GONE
    }

    private fun handleResult(result: Resource<List<Table>?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val listTableItemVM = mutableListOf<TableItemVM>()
                result.data!!.forEach { table ->
                    listTableItemVM.add(
                        TableItemVM(
                            container,
                            this@TableStatusVM,
                            table
                        )
                    )
                }
                _liveListTableItemVM.value = listTableItemVM
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }

    fun onItemClick(order: Order) {
        container.loadFragment(
            AllMainFragment.OrderDetailTable.createFragment(order.id!!),
            true
        )
    }

    fun onAddTableButton() {
        container.loadFragment(AllMainFragment.TableDetail.createFragment(), true)
    }
}