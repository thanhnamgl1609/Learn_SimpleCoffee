package com.project.simplecoffee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseUser
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
        binding.viewModel = viewModel

        bindButton(viewModel.user.value)

        viewModel.user.observe(this, {
            bindButton(it)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkLogInStatus()
    }

    private fun bindButton(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            binding.myBtn.apply {
                setOnClickListener {
                    viewModel.signOut()
                }
                text = "Sign out"
            }
            viewModel.getUserInfo()
        } else {
            binding.myBtn.apply {
                setOnClickListener {
                    val intent = Intent(this.context, SignInActivity::class.java)
                    startActivity(intent)
                }
                text = "Sign in"
            }
        }
    }
}