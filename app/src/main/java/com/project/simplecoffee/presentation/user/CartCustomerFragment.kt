package com.project.simplecoffee.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentCartCustomerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartCustomerFragment : Fragment() {

    @Inject
    lateinit var cartCustomerVM: CartCustomerVM
    private lateinit var binding: FragmentCartCustomerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart_customer,
                container, false
            )
        binding.apply {
            viewModel = cartCustomerVM
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onStart() {
        cartCustomerVM.apply {
            loadCart()
        }
        super.onStart()
    }

    override fun onDestroy() {
        cartCustomerVM.save()
        super.onDestroy()
    }
}