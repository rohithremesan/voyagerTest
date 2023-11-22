package com.example.voyager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.voyager.R
import com.example.voyager.databinding.FragmentSignupBinding
import com.example.voyager.view_model.SignUpViewModel
import com.example.voyager.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel :UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignupBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.viewModel=viewModel
        return binding.root
    }


}