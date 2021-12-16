package com.project.simplecoffee.presentation.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.simplecoffee.databinding.ActivitySignInBinding
import com.project.simplecoffee.utils.common.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), AuthContainer {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showError(message: String) {
        makeToast(message)
    }

    override fun finishActivity() {
        finish()
    }
}