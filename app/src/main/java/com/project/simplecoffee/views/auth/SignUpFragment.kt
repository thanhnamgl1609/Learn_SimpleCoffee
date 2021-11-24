package com.project.simplecoffee.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentSignUpBinding
import com.project.simplecoffee.viewmodel.UserVM
import javax.inject.Inject

class SignOutFragment : Fragment() {

    @Inject
    lateinit var userVM: UserVM
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        setUpSpinner()
        return binding.root
    }


    private fun setUpSpinner() {
        val spinner: Spinner = binding.inputGender
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.gender_array,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
}