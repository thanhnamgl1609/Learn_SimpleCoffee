package com.project.simplecoffee.presentation.contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.project.simplecoffee.R
import com.project.simplecoffee.databinding.FragmentContactDetailBinding
import com.project.simplecoffee.domain.model.Contact
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ContactDetailFragment : Fragment() {

    @Inject
    lateinit var contactDetailVM: ContactDetailVM
    private lateinit var binding: FragmentContactDetailBinding
    var contact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_contact_detail, container, false)
        binding.apply {
            viewModel = contactDetailVM
            lifecycleOwner = viewLifecycleOwner
        }
        contact?.apply { contactDetailVM.setContact(this) }
        return binding.root
    }
}