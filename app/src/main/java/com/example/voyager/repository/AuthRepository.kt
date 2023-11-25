package com.example.voyager.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.voyager.model.User
import com.example.voyager.view.utility.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun register(email: String, password: String, user: User): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())

        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firestore.collection("User")
                .document(firebaseAuth.currentUser!!.uid)
                .set(user).await()

            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))

        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }


    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun login(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {

        emit(Resource.Loading())

        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))
           // loggedOutLiveData.postValue(false)

        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }

    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun getLoggedUser(): Flow<Resource<FirebaseUser>> = flow {

        emit(Resource.Loading())

        if (firebaseAuth.currentUser != null) {
            emit(Resource.Success(data = firebaseAuth.currentUser!!))
        } else {
            emit(Resource.Error("Not Logged"))
        }

    }

}