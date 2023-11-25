package com.example.voyager.model

import com.google.firebase.firestore.DocumentReference

data class ProductListResponce(
    val data:List<Product>?=null,
    val error: String = "",
    val isLoading: Boolean = false
)
