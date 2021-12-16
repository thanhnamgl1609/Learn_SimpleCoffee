package com.project.simplecoffee.presentation.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment() {

    @Inject
    lateinit var menuVM: MenuVM
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = menuVM
            CategoriesRecyclerView.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.HORIZONTAL,
                    false
                )
            searchBar.apply {
                setOnQueryTextListener(menuVM.searchViewListener)
                isSubmitButtonEnabled = true
            }
        }
        return binding.root
    }

    override fun onStart() {
        menuVM.checkLogInStatus()
        super.onStart()
    }
}
