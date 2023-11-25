package com.example.voyager.model

import com.google.firebase.firestore.DocumentReference

data class ProductUpload(
    val data: DocumentReference?=null,
    val error: String = "",
    val isLoading: Boolean = false)
