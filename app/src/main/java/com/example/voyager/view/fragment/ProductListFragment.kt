package com.example.voyager.view.fragment


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voyager.R
import com.example.voyager.databinding.FragmentProductListBinding
import com.example.voyager.model.ProductUpload
import com.example.voyager.view.adapter.ProductListAdapter
import com.example.voyager.view.utility.State
import com.example.voyager.view_model.ProductViewModel
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch


class ProductListFragment : BaseFragment() {
    private lateinit var binding:FragmentProductListBinding
    private val viewModel:ProductViewModel by viewModels()
    private val adapter:ProductListAdapter= ProductListAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProductListBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.layoutManager = LinearLayoutManager(view.context)
        binding.rvList.adapter=adapter
        observe()
        viewModel.getProducts("Men")



    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun observe() {
        dropdownListner()
        lifecycleScope.launch {
            viewModel.productList.collect {
                if (it.isLoading) {
                    binding.progressBar.progress.visibility=View.VISIBLE
                }
                if (it.error.isNotBlank()) {
                    binding.progressBar.progress.visibility=View.GONE
                    showSnackBar(""+it.error)
                }
                it.data?.let {
                    binding.progressBar.progress.visibility=View.GONE
                    adapter.setProductList(it)
                    adapter.notifyDataSetChanged()

                }
            }

        }

    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun dropdownListner() {
        val autoCompleteTextView = binding.categoryDropdown
        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                // Handle the item selection here
                val selectedCategory = parent.getItemAtPosition(position) as String
                viewModel.getProducts(selectedCategory)

            }

    }

    private fun setCategoryDropDown() {
        val category = resources.getStringArray(R.array.category)
        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, category)
        binding.categoryDropdown.setAdapter(categoryAdapter)
        binding.categoryDropdown.setText(category[0],false)
    }

    override fun onResume() {
        super.onResume()
        setCategoryDropDown()
    }

}