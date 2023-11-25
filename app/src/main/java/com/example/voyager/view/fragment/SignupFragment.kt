package com.example.voyager.view.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.voyager.R
import com.example.voyager.databinding.FragmentSignupBinding
import com.example.voyager.view.utility.callbacks.ClickHelper
import com.example.voyager.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : BaseFragment(),TextWatcher {
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
        binding.clickHelper=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextListners()
        observe()
        viewModel.loggedUser()
    }

    private fun observe() {

        viewModel.loggedIn.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
            }
        })



        lifecycleScope.launch {
            viewModel.user.collect {
                if (it.isLoading) {
                   binding.progressBar.progress.visibility=View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.progressBar.progress.visibility=View.GONE
                    showSnackBar(""+it.error)
                }
                it.data?.let {
                    binding.progressBar.progress.visibility=View.GONE
                    showSnackBar(getString(R.string.sinup_sucess))
                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)

                }
            }

        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onClick(view: View) {
        super.onClick(view)
        when(view.id){
            R.id.signUp->{
                if (isValidEntry()){
                    viewModel.register()
                }
            }
            R.id.sign_in->{

                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
            else->{
                hideKeyboard()
            }
        }

    }

    private fun hideError() {

        binding.name.error=null
        binding.email.error=null
        binding.password.error=null
        binding.confirmPassword.error=null

    }


    private fun isValidEntry():Boolean{


        if (binding.name.editText?.text.isNullOrEmpty()){
            binding.name.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.email.editText?.text.isNullOrEmpty()){
            binding.email.error=getString(R.string.field_null_error)
            return false
        }else{
            if (!isValidEmail(binding.email.editText?.text)){
                binding.email.error=getString(R.string.email_error)
                return false
            }
        }
        if (binding.password.editText?.text.isNullOrEmpty()){
            binding.password.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.confirmPassword.editText?.text.isNullOrEmpty()){
            binding.confirmPassword.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.confirmPassword.editText?.text.toString()!=binding.password.editText?.text.toString()){
            binding.password.error=getString(R.string.password_not_match_error)
            binding.confirmPassword.error=getString(R.string.password_not_match_error)
            return false
        }
        return true

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
       hideError()
    }

    private fun addTextListners() {
        binding.email.editText?.addTextChangedListener(this)
        binding.password.editText?.addTextChangedListener(this)
        binding.name.editText?.addTextChangedListener(this)
        binding.confirmPassword.editText?.addTextChangedListener(this)
    }


}