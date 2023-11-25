package com.example.voyager.view_model

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyager.model.AuthState
import com.example.voyager.model.User
import com.example.voyager.repository.AuthRepository
import com.example.voyager.view.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor( private val authRepository: AuthRepository) :ViewModel() {
    var name:String=""
    var email:String=""
    var password:String=""
    var confirm_password:String=""

    private val _user = MutableStateFlow(AuthState())
    val user: StateFlow<AuthState> = _user
    private val _loggedIn =MutableLiveData<Boolean>(false)
    val loggedIn:LiveData<Boolean> =_loggedIn



    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun login() {
        authRepository.login(email, password).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun register() {
        authRepository.register(email, password, User(name,email)).onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout(){
        authRepository.logOut()
    }

    fun loggedUser() {

        authRepository.getLoggedUser().onEach {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
                is Resource.Success -> {
                  _loggedIn.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }


}