package com.project.simplecoffee.presentation.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentOrderTrackingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderTrackingFragment : Fragment() {

    @Inject
    lateinit var orderTrackingVM: OrderTrackingVM
    private lateinit var binding: FragmentOrderTrackingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_tracking,
            container,
            false
        )
        binding.apply {
            viewModel = orderTrackingVM
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}