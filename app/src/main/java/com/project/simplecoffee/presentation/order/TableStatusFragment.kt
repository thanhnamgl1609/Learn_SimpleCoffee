package com.project.simplecoffee.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentTableStatusBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TableStatusFragment : Fragment() {

    @Inject
    lateinit var tableStatusVM: TableStatusVM
    private lateinit var binding: FragmentTableStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_table_status,
                container, false
            )
        binding.apply {
            viewModel = tableStatusVM
            lifecycleOwner = viewLifecycleOwner
            recyclerviewTables.layoutManager = GridLayoutManager(activity, 3)
        }
        return binding.root
    }
}