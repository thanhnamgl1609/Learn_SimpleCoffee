package com.project.simplecoffee.data.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepo {
    // Login, logout and register function
    private var application: Application? = null
    private var firebaseUserMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private var userLoggedMutableLiveData: MutableLiveData<Boolean>? = null
    private var auth: FirebaseAuth? = null

    open fun login(email: String?, pass: String?) {
        auth?.signInWithEmailAndPassword(email.toString(), pass.toString())
            ?.addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        firebaseUserMutableLiveData?.postValue(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            application,
                            task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}