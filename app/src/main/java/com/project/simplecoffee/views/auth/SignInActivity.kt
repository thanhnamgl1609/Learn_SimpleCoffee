package com.project.simplecoffee.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.SignInActivityBinding
import com.project.simplecoffee.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

//        viewModel.getLoggedStatus().observe(
//            viewLifecycleOwner,
//            {
//
//            }
//        )

    }

    override fun onStart() {
        super.onStart()

    }
}