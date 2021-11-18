package com.mmunoz.todo.domain.repositories

import android.location.Location
import android.net.Uri
import com.mmunoz.todo.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun delete(key: String, currentImages: List<String>): Flow<Response<Unit>>

    suspend fun add(
        name: String,
        description: String,
        images: List<Uri>,
        location: Location? = null
    ): Flow<Response<Unit>>

    suspend fun edit(
        key: String,
        name: String,
        description: String,
        images: List<Uri>,
        currentImages: List<String>
    ): Flow<Response<Unit>>
}