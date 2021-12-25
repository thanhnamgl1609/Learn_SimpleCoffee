package com.project.simplecoffee.presentation.order

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.simplecoffee.R
import com.project.simplecoffee.domain.model.Table
import com.project.simplecoffee.domain.model.details.TableShape
import com.project.simplecoffee.domain.usecase.order.CreateTableUseCase
import com.project.simplecoffee.domain.usecase.order.DeleteTableUseCase
import com.project.simplecoffee.presentation.common.main.MainContainer
import com.project.simplecoffee.utils.common.Resource
import com.project.simplecoffee.utils.constant.SuccessConst
import kotlinx.coroutines.launch
import javax.inject.Inject

class TableDetailVM @Inject constructor(
    private val container: MainContainer,
    private val getTableByIDUseCase: GetTableByIDUseCase,
    private val updateTableDetailUseCase: UpdateTableDetailUseCase,
    private val createTableUseCase: CreateTableUseCase,
    private val deleteTableUseCase: DeleteTableUseCase
) : ViewModel() {
    private var size: Int? = null
    private var shape: TableShape? = null
    private var tableID: String? = null
    private val _removeVisibility = MutableLiveData(View.GONE)

    val name = MutableLiveData("")
    val selectedSizePosition = MutableLiveData(-1)
    val selectedShapePosition = MutableLiveData(-1)
    val removeVisibility: LiveData<Int>
        get() = _removeVisibility

    fun setTableID(
        tableID: String?,
        context: Context,
        spinnerShape: Spinner,
        spinnerSize: Spinner
    ) {
        viewModelScope.launch {
            this@TableDetailVM.tableID = tableID
            if (tableID != null) {
                _removeVisibility.value = View.VISIBLE
                when (val result = getTableByIDUseCase(tableID)) {
                    is Resource.OnSuccess -> {
                        val table = result.data!!
                        size = table.size
                        shape = table.shape
                        name.postValue(table.name)
                    }
                    is Resource.OnFailure -> {
                        container.showMessage(result.message.toString())
                    }
                }
            }
            setTableSpinner(
                context,
                spinnerShape,
                spinnerSize
            )
        }
    }

    fun onDoneButton() {
        viewModelScope.launch {
            tableID?.let { id ->
                handleResult(
                    updateTableDetailUseCase(id, name.value, size, shape),
                    SuccessConst.SUCCESS_UPDATE
                )
            } ?: handleResult(
                createTableUseCase(name.value, size, shape),
                SuccessConst.SUCCESS_CREATE
            )
        }
    }

    fun onRemoveButton() {
        viewModelScope.launch {
            handleResult(deleteTableUseCase(tableID!!), SuccessConst.SUCCESS_DELETE)
        }
    }

    fun setTableSpinner(
        context: Context,
        spinnerShape: Spinner,
        spinnerSize: Spinner
    ) {
        val tableSizes = mutableListOf("4", "6")
        ArrayAdapter(
            context,
            R.layout.spinner_item,
            tableSizes
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerSize.adapter = adapter
            selectedSizePosition.value = tableSizes.indexOf(size.toString())
            spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val tableShapes =
                        mutableListOf(TableShape.Rectangle.value)
                    size = tableSizes[position].toInt()
                    if (size == 4) {
                        tableShapes.add(TableShape.Circle.value)
                    }
                    this@TableDetailVM.setShapeSpinner(context, spinnerShape, tableShapes)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    size = null
                    this@TableDetailVM.setShapeSpinner(context, spinnerSize, emptyList())
                }
            }
        }
    }

    private fun setShapeSpinner(
        context: Context,
        spinnerShape: Spinner,
        tableShapes: List<String>
    ) {
        ArrayAdapter(
            context,
            R.layout.spinner_item,
            tableShapes
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerShape.adapter = adapter
            selectedShapePosition.value = tableShapes.indexOf(shape?.value)
            spinnerShape.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    shape = TableShape.getTableShape(tableShapes[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    shape = null
                }
            }
        }
    }

    private fun handleResult(result: Resource<Table?>, onSuccessMessage: String) {
        when (result) {
            is Resource.OnSuccess -> {
                container.showMessage(onSuccessMessage)
                container.backToPreviousFragment()
            }
            is Resource.OnFailure -> {
                container.showMessage(result.message.toString())
            }
        }
    }
}