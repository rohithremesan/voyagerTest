package com.example.voyager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.voyager.R
import com.example.voyager.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :Fragment() {
private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }


}