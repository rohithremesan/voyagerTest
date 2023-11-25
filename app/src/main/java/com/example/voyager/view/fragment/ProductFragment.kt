package com.example.voyager.view.fragment

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.example.voyager.R
import com.example.voyager.databinding.FragmentProductBinding
import com.example.voyager.model.ProductUpload
import com.example.voyager.view_model.ProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductFragment : BaseFragment(),TextWatcher {
    private lateinit var binding:FragmentProductBinding
    private val viewModel:ProductViewModel by viewModels()
    private var uri:Uri?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentProductBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.viewModel=viewModel
        binding.clickHelper=this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        addTextListners()
    }

    private fun setCategoryDropDown() {
        val category = resources.getStringArray(R.array.category)
        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)
        binding.categoryDropdown.setAdapter(categoryAdapter)
        binding.categoryDropdown.setText(category[0],false)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onClick(view: View) {
        super.onClick(view)
        when(view.id){

            R.id.imageView->{
                ImagePicker.with(this)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            R.id.add->{
                if (uri!=null){
                    if (isValidProduct()){
                        viewModel.category=binding.categoryDropdown.text.toString()
                        viewModel.addProduct(uri!!)
                    }
                }else{
                    showSnackBar(getString(R.string.please_add_image))

                }

            }
            else->{
                hideKeyboard()
            }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

               // mProfileUri = fileUri
                uri=fileUri
                binding.imageView.setImageURI(fileUri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.e("ImagePicker",ImagePicker.getError(data))
            } else {

            }
        }

    private  fun observe() {
        lifecycleScope.launch {
            viewModel.product.collect {
                if (it.isLoading) {
                    binding.progressBar.progress.visibility=View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.progressBar.progress.visibility=View.GONE
                    showSnackBar(""+it.error)
                }
                it.data?.let {

                    if (it is DocumentReference){
                        viewModel._product.value=ProductUpload()
                        binding.progressBar.progress.visibility=View.GONE
                        viewModel.resetvalues()
                        uri=null
                        showSnackBar(getString(R.string.product_add_sucess))
                        navigateToProductListFragment()
                    }



                }
            }

        }


    }

    private fun navigateToProductListFragment() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.productListFragment
    }

    private fun hideError() {
        binding.tilCode.error=null
        binding.tilProduct.error=null
        binding.tilPrice.error=null
        binding.tilDesc.error=null

    }

    private fun isValidProduct():Boolean{

        if (binding.tilCode.editText?.text.isNullOrEmpty()){
            binding.tilCode.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.tilProduct.editText?.text.isNullOrEmpty()){
            binding.tilProduct.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.tilPrice.editText?.text.isNullOrEmpty()){
            binding.tilPrice.error=getString(R.string.field_null_error)
            return false
        }
        if (binding.tilDesc.editText?.text.isNullOrEmpty()){
            binding.tilDesc.error=getString(R.string.field_null_error)
            return false
        }
        return true

    }

    override fun onResume() {
        super.onResume()
        setCategoryDropDown()
    }

    private fun addTextListners() {
        binding.tilCode.editText?.addTextChangedListener(this)
        binding.tilProduct.editText?.addTextChangedListener(this)
        binding.tilPrice.editText?.addTextChangedListener(this)
        binding.tilDesc.editText?.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        hideError()
    }


}