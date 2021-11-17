package com.mmunoz.todo.data.managers

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebasePhotosManager @Inject constructor() {

    private val firebaseStorage = FirebaseStorage.getInstance()

    suspend fun uploadPhotos(photosUri: List<Uri>): List<String> {
        val uploadedPhotosUriLink = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            photosUri.map { uri ->
                async(Dispatchers.IO) {
                    uploadPhoto(uri)
                }
            }
        }.awaitAll()
        return uploadedPhotosUriLink.map { it.toString() }
    }

    suspend fun clearPhotosByKey(images: List<String>) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            images.map { imageUrl ->
                async(Dispatchers.IO) {
                    FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                        .delete()
                        .await()
                }
            }
        }.awaitAll()
    }

    suspend fun deletePhoto(image: String) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            FirebaseStorage.getInstance().getReferenceFromUrl(image)
                .delete()
                .await()
        }
    }

    private suspend fun uploadPhoto(fileUri: Uri): Uri {
        val fileName = UUID.randomUUID().toString()
        return firebaseStorage.reference.child(fileName)
            .putFile(fileUri)
            .await()
            .storage
            .downloadUrl
            .await()
    }
}