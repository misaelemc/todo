package com.mmunoz.todo.domain.repositories

import android.net.Uri
import com.mmunoz.todo.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun delete(key: String, oldImage: String) : Flow<Response<Void>>

    suspend fun add(name: String, description: String, image: Uri) : Flow<Response<Void>>

    suspend fun edit(
        key: String,
        name: String,
        description: String,
        image: Uri,
        oldImage: String
    ) : Flow<Response<Void>>
}