package com.mmunoz.todo.data.repositories

import android.location.Location
import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mmunoz.todo.data.managers.FirebasePhotosManager
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.models.TaskItem
import com.mmunoz.todo.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firebasePhotosManager: FirebasePhotosManager
) : TaskRepository {

    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference(TASKS_REFERENCE)
    }

    override suspend fun delete(
        key: String,
        currentImages: List<String>
    ): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            firebasePhotosManager.clearPhotosByKey(currentImages)
            val response = dbReference.child(key).removeValue().await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    override suspend fun add(
        name: String,
        description: String,
        images: List<Uri>,
        location: Location?
    ): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            val urlPhotos = firebasePhotosManager.uploadPhotos(images)
            val data = TaskItem(name, description, urlPhotos, location?.latitude, location?.longitude)
            val response = dbReference.push().setValue(data).await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    override suspend fun edit(
        key: String,
        name: String,
        description: String,
        images: List<Uri>,
        currentImages: List<String>
    ): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            val urlPhotos = firebasePhotosManager.uploadPhotos(images)
            val allImages = ArrayList<String>().apply {
                addAll(currentImages)
                addAll(urlPhotos)
            }
            val task = HashMap<String, Any>().apply {
                put(NAME_FIELD, name)
                put(IMAGES_FIELD, allImages)
                put(DESCRIPTION_FIELD, description)
            }
            val response = dbReference.child(key).updateChildren(task).await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    companion object {
        const val TASKS_REFERENCE = "tasks"
        const val NAME_FIELD = "name"
        const val IMAGES_FIELD = "images"
        const val DESCRIPTION_FIELD = "description"
    }
}