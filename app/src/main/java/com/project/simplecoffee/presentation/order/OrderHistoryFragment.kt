package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentOrderHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {

    @Inject
    lateinit var orderHistoryVM: OrderHistoryVM
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var datePicker: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_history,
            container,
            false
        )
        initDatePicker()
        binding.apply {
            viewModel = orderHistoryVM
            lifecycleOwner = viewLifecycleOwner
            thisDatePicker.setOnClickListener {
                datePicker.show()
            }
        }
        return binding.root
    }

    private fun initDatePicker() {
        val cal = Calendar.getInstance()
        datePicker = DatePickerDialog(
            requireContext(),
            orderHistoryVM.mThisDateListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }
}