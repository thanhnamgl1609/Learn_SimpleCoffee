package com.project.simplecoffee.views.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.project.simplecoffee.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.project.simplecoffee.R
import com.project.simplecoffee.common.makeToast
import com.project.simplecoffee.viewmodels.MainVM
import com.project.simplecoffee.views.auth.AuthActivity
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContainer {

    @Inject
    lateinit var viewModel : MainVM
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment = AccountInfoFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_menu -> {

                    return@setOnItemSelectedListener true
                }
                R.id.action_order -> {
                    return@setOnItemSelectedListener true
                }
                R.id.action_more -> {
                    viewModel.loadFragment(MainFragment.AccountInfo)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    override fun showMessage(message: String) {
        makeToast(message)
    }

    override fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSignIn() {
        startActivity(Intent(this, AuthActivity::class.java))
    }
}