package com.project.simplecoffee.presentation.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentOrderProcessingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderProcessingFragment : Fragment() {

    @Inject
    lateinit var orderProcessingVM: OrderProcessingVM
    private lateinit var binding: FragmentOrderProcessingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_processing,
            container,
            false
        )
        binding.apply {
            viewModel = orderProcessingVM
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}