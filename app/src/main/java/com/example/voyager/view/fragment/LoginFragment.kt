package com.example.voyager.view.fragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.voyager.R
import com.example.voyager.databinding.FragmentLoginBinding
import com.example.voyager.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment :BaseFragment(),TextWatcher {
private lateinit var binding: FragmentLoginBinding
private val viewModel:UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.clickHelper=this
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onClick(view: View) {
        super.onClick(view)
        when(view.id){
            R.id.signUp->{
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
            R.id.sign_in->{
                if (isValidEntry()){
                    viewModel.login()
                }

            }
            else->{
                hideKeyboard()
            }

        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextListners()
        observe()


    }

    private fun addTextListners() {
        binding.email.editText?.addTextChangedListener(this)
        binding.password.editText?.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        hideError()
    }

    private fun hideError() {
        binding.email.error=null
        binding.password.error=null
    }


    private fun isValidEntry():Boolean{

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
        return true

    }

    private fun observe() {
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
                    showSnackBar(getString(R.string.login_sucess))
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                }
            }

        }
    }

}