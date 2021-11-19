package com.project.simplecoffee.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentSignInBinding
import com.project.simplecoffee.viewmodels.UserVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var viewModel: UserVM
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.auth = viewModel
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignUp.setOnClickListener {
            navController.navigate(R.id.signUpFragment)
        }
    }
}