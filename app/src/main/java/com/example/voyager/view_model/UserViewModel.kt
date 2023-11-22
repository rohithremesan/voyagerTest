package com.example.voyager.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor() :ViewModel() {
    var name:String=""
    var email:String=""
    var passwoed:String=""
    var confirm_password:String=""

}