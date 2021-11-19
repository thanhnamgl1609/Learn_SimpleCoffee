package com.project.simplecoffee.views.auth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.simplecoffee.R
import com.project.simplecoffee.common.makeToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), AuthContainer{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    override fun showError(message: String) {
        makeToast(message)
    }

    override fun finish() {
        finish()
    }
}