package com.example.voyager.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.voyager.R
import com.example.voyager.databinding.FragmentProductBinding
import com.example.voyager.databinding.FragmentProfileBinding
import com.example.voyager.view_model.UserViewModel

class ProfileFragment : BaseFragment() {
private lateinit var binding:FragmentProfileBinding
private val viewModel:UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentProfileBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.clickHelper=this
        return binding.root
    }


    override fun onClick(view: View) {
        super.onClick(view)
        viewModel.logout()
        val intent = activity?.intent
        activity?. finish()
        if (intent != null) {
            startActivity(intent)
        }


    }


}