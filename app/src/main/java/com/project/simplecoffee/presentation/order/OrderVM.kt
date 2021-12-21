package com.project.simplecoffee.presentation.order

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.domain.model.Order
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import kotlinx.coroutines.launch

abstract class OrderVM constructor(
    private val container: MainContainer
) : ViewModel() {
    private val _loadingVisible = MutableLiveData(View.GONE)
    private val _emptyTextVisible = MutableLiveData(View.GONE)
    val liveListOrderItemVM: LiveData<List<OrderItemVM>>
        get() = _liveListOrderItemVM

    private val _liveListOrderItemVM = MutableLiveData<List<OrderItemVM>>()
    val loadingVisible: LiveData<Int>
        get() = _loadingVisible
    val emptyTextVisible: LiveData<Int>
        get() = _emptyTextVisible

    abstract suspend fun getOrder(): Resource<List<Order>?>

    protected fun beforeLoading() {
        _emptyTextVisible.value = View.GONE
        _liveListOrderItemVM.value = emptyList()
        _loadingVisible.postValue(View.VISIBLE)
    }

    protected fun afterLoading() {
        if (_liveListOrderItemVM.value!!.isEmpty()) {
            _emptyTextVisible.value = View.VISIBLE
        }
        _loadingVisible.postValue(View.GONE)
    }

    protected fun getListOrder() =
        viewModelScope.launch {
            beforeLoading()
            handleResult(getOrder())
            afterLoading()
        }

    protected fun handleResult(result: Resource<List<Order>?>) {
        when (result) {
            is Resource.OnSuccess -> {
                val listOrderItemVM = mutableListOf<OrderItemVM>()
                result.data?.forEach { order ->
                    listOrderItemVM.add(
                        OrderItemVM(
                            container,
                            order
                        )
                    )
                }
                _liveListOrderItemVM.value = listOrderItemVM
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }
}