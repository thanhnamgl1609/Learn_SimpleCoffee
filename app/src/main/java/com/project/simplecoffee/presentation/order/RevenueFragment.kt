package com.project.simplecoffee.presentation.order

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentRevenueBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RevenueFragment : Fragment() {

    @Inject
    lateinit var revenueVM: RevenueVM
    private lateinit var binding: FragmentRevenueBinding
    private lateinit var datePicker: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_revenue,
            container,
            false
        )
        initDatePicker()
        binding.apply {
            viewModel = revenueVM
            lifecycleOwner = viewLifecycleOwner
            fromDate.setOnClickListener {
                datePicker.setOnDateSetListener(revenueVM.mFromDateListener)
                datePicker.show()
            }
            toDate.setOnClickListener {
                datePicker.setOnDateSetListener(revenueVM.mToDateListener)
                datePicker.show()
            }
            thisDatePicker.setOnClickListener {
                datePicker.setOnDateSetListener(revenueVM.mThisDateListener)
                datePicker.show()
            }

        }
        return binding.root
    }

    private fun initDatePicker() {
        val cal = Calendar.getInstance()
        datePicker = DatePickerDialog(
            requireContext(),
            revenueVM.mThisDateListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }
}