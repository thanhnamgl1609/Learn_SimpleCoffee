package com.project.simplecoffee.presentation.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentCartCustomerBinding
import com.project.simplecoffee.databinding.FragmentCartStaffBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartStaffFragment : Fragment() {

    @Inject
    lateinit var cartStaffVM: CartStaffVM
    private lateinit var binding: FragmentCartStaffBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart_staff,
                container, false
            )
        binding.apply {
            viewModel = cartStaffVM
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onStart() {
        cartStaffVM.setTableSpinner(requireContext(), binding.selectTable)
        super.onStart()
    }

    override fun onStop() {
        cartStaffVM.save()
        super.onStop()
    }
}