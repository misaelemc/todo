package com.mmunoz.todo.data.repositories

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {

    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference(TASKS_REFERENCE)
    }

    override suspend fun delete(key: String, oldImage: String): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            FirebaseStorage.getInstance().getReference("images/${oldImage}")
                .delete()
                .await()
            val response = dbReference.child(key).removeValue().await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    override suspend fun add(
        name: String,
        description: String,
        image: Uri
    ): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            val imageName = getImageFile()
            val taskMap = HashMap<String, String>().apply {
                put(NAME_FIELD, name)
                put(IMAGE_FIELD, imageName)
                put(DESCRIPTION_FIELD, description)
            }
            FirebaseStorage.getInstance().getReference("images/${imageName}")
                .putFile(image)
                .await()
            val response = dbReference.push().setValue(taskMap).await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    override suspend fun edit(
        key: String,
        name: String,
        description: String,
        image: Uri,
        oldImage: String
    ): Flow<Response<Void>> = flow {
        try {
            emit(Response.Loading)
            val task = HashMap<String, Any>().apply {
                put(NAME_FIELD, name)
                put(IMAGE_FIELD, image.path.orEmpty())
                put(DESCRIPTION_FIELD, description)
            }
            val imageName = getImageFile()
            FirebaseStorage.getInstance().getReference("images/${oldImage}")
                .delete()
                .await()

            FirebaseStorage.getInstance().getReference("images/${imageName}")
                .putFile(image)
                .await()

            val response = dbReference.child(key).updateChildren(task).await()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(e.message.orEmpty()))
        }
    }

    private fun getImageFile(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        return formatter.format(Date())
    }

    companion object {
        const val TASKS_REFERENCE = "tasks"
        const val NAME_FIELD = "name"
        const val IMAGE_FIELD = "image"
        const val DESCRIPTION_FIELD = "description"
    }
}