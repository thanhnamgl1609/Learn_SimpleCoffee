package com.project.simplecoffee.views.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.SignInActivityBinding
import com.project.simplecoffee.databinding.SignInFragmentBinding
import com.project.simplecoffee.viewmodels.ShopViewModel

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private var _binding: SignInFragmentBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        // TODO: Use the ViewModel
    }

}