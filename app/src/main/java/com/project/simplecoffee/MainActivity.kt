package com.project.simplecoffee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.project.simplecoffee.databinding.ActivityMainBinding
import com.project.simplecoffee.viewmodels.MainViewModel
import com.project.simplecoffee.views.auth.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModel.loggedStatus.observe(this,{
            bindButton(it)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkLogInStatus()
    }

    private fun bindButton(logged : Boolean) {
        if (logged) {
            binding.myBtn.setOnClickListener {
                viewModel.signOut()
            }
            binding.myBtn.text = "Sign out"
        } else {
            binding.myBtn.setOnClickListener{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            binding.myBtn.text = "Sign in"
        }
    }
}