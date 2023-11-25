package com.example.voyager.repository

import android.net.Uri
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.voyager.model.Product
import com.example.voyager.model.ProductUpload
import com.example.voyager.view.utility.Resource
import com.example.voyager.view.utility.State
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class ProductRepository @Inject constructor( private val storage: FirebaseStorage,private val firestore: FirebaseFirestore){

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun addProduct(uri: Uri, product: Product) : Flow<Resource<DocumentReference>> =flow {

        emit(Resource.Loading())
        try {
            val imagesRef: StorageReference = storage.getReference("images")
            val ref = imagesRef.child(getRandomText(10))
            val uploadTask = ref.putFile(uri).await()
            val downloadedUrl = ref.downloadUrl.await()
            product.imageUrl = downloadedUrl.toString()
            val doc = firestore.collection("products").add(product).await()
            doc.update("id", doc.id).await()
            emit(Resource.Success(doc))
        }catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }

    }


    /*fun getProducts(category:String) = flow<State<List<Product>>>{
        emit(State.loading())
        val snapshot = firestore.collection("products").whereEqualTo("category", category)
            .get().await()
        val posts = snapshot.toObjects(Product::class.java)
        emit(State.success(posts))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

     */



    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getProducts(category: String) : Flow<Resource<List<Product>>> =flow {

        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection("products").whereEqualTo("category", category)
                .get().await()
            val posts = snapshot.toObjects(Product::class.java)
            Log.d("TAG",""+posts)
            emit(Resource.Success(posts))
        }catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }

    }



    private fun getRandomText(length: Int): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val randomText = StringBuilder()

        for (i in 0 until length) {
            randomText.append(alphabet[Random.nextInt(alphabet.length)])
        }

        return randomText.toString()
    }

}