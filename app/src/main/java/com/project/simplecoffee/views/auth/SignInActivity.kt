package com.project.simplecoffee.views.auth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.simplecoffee.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }
}