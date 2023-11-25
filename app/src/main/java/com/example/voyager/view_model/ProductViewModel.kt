package com.example.voyager.view_model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.voyager.model.AuthState
import com.example.voyager.model.Product
import com.example.voyager.model.ProductListResponce
import com.example.voyager.model.ProductUpload
import com.example.voyager.repository.ProductRepository
import com.example.voyager.view.utility.Resource
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor( private val productRepository: ProductRepository):ViewModel() {
    var code: String=""
    var name: String=""
    var description: String=""
    var price:String=""
    var category:String=""

     val _product = MutableStateFlow(ProductUpload())
    val product: StateFlow<ProductUpload> = _product
    private val _productList = MutableStateFlow(ProductListResponce())
    val productList: StateFlow<ProductListResponce> = _productList

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun addProduct(uri: Uri) {
        productRepository.addProduct(uri,Product("",code,name,description,"image",category,price)).onEach {
            when (it) {
                is Resource.Loading -> {
                    _product.value = ProductUpload(isLoading = true)
                }
                is Resource.Error -> {
                    _product.value = ProductUpload(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _product.value = ProductUpload(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getProducts(category:String) {
        productRepository.getProducts(category).onEach {
            when (it) {
                is Resource.Loading -> {
                    _productList.value = ProductListResponce(isLoading = true)
                }
                is Resource.Error -> {
                    _productList.value = ProductListResponce(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _productList.value = ProductListResponce(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun resetvalues(){
        code=""
        name=""
        description=""
        price=""
    }





}