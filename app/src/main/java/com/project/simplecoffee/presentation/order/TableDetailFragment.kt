package com.project.simplecoffee.presentation.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentTableDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TableDetailFragment : Fragment() {

    @Inject
    lateinit var tableDetailVM: TableDetailVM
    private lateinit var binding: FragmentTableDetailBinding
    var tableID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_table_detail,
            container,
            false
        )
        binding.apply {
            viewModel = tableDetailVM
            lifecycleOwner = viewLifecycleOwner
        }
        tableDetailVM.setTableID(tableID,
            requireContext(),
            binding.tableShape,
            binding.tableSize
        )
        return binding.root
    }

}