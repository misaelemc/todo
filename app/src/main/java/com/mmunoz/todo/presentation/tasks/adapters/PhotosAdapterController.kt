package com.mmunoz.todo.presentation.tasks.adapters

import android.net.Uri
import com.airbnb.epoxy.AsyncEpoxyController
import com.mmunoz.todo.presentation.views.photoImageView

class PhotosAdapterController : AsyncEpoxyController() {

    var data: List<Uri> = listOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        data.forEach { photo ->
            photoImageView {
                id(photo.hashCode())
                data(photo)
                spanSizeOverride { _, _, _ -> 1 }
            }
        }
    }

    fun remove(data: Uri) {
        this.data = ArrayList(this.data.filter { it.toString() != data.toString() })
    }

    fun append(data: Uri) {
        this.data = ArrayList(this.data).apply {
            add(data)
        }
    }

    fun append(data: List<Uri>) {
        this.data = ArrayList(this.data).apply {
            addAll(data)
        }
    }
}