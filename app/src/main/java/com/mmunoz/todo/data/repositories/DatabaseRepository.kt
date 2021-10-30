package com.mmunoz.todo.data.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DatabaseRepository @Inject constructor() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val dbReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("tasks")
    }

    fun login(user: String, password: String, response: (error: String?) -> Unit) {
        auth.signInWithEmailAndPassword(user, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                response.invoke(null)
            } else {
                response.invoke(task.exception?.message ?: "Error")
            }
        }
    }

    fun register(username: String, password: String, response: (error: String?) -> Unit) {
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                response.invoke(null)
            } else {
                response.invoke(task.exception?.message ?: "Error")
            }
        }
    }

    fun addTask(
        name: String,
        description: String,
        image: Uri,
        onCompleted: (error: String?) -> Unit
    ) {
        val imageName = getImageFile()
        val task = HashMap<String, String>().apply {
            put("name", name)
            put("image", imageName)
            put("description", description)
        }
        FirebaseStorage.getInstance().getReference("images/${imageName}")
            .putFile(image)
            .addOnSuccessListener {
                dbReference
                    .push()
                    .setValue(task)
                    .addOnSuccessListener { onCompleted.invoke(null) }
                    .addOnFailureListener { onCompleted.invoke(it.message ?: "Error") }
            }
            .addOnFailureListener { onCompleted.invoke(it.message ?: "Error") }
    }

    fun editTask(
        key: String,
        name: String,
        description: String,
        image: Uri,
        oldImage: String,
        onCompleted: (error: String?) -> Unit
    ) {
        val task = HashMap<String, Any>().apply {
            put("name", name)
            put("image", image.path.orEmpty())
            put("description", description)
        }
        val imageName = getImageFile()
        FirebaseStorage.getInstance().getReference("images/${oldImage}")
            .delete()
        FirebaseStorage.getInstance().getReference("images/${imageName}")
            .putFile(image)
            .addOnSuccessListener {
                dbReference.child(key)
                    .updateChildren(task)
                    .addOnSuccessListener { onCompleted.invoke(null) }
                    .addOnFailureListener { onCompleted.invoke(it.message ?: "Error") }
            }
            .addOnFailureListener { onCompleted.invoke(it.message ?: "Error") }
    }

    fun deleteTask(key: String, oldImage: String, onCompleted: (error: String?) -> Unit) {
        FirebaseStorage.getInstance().getReference("images/${oldImage}")
            .delete()
        dbReference.child(key)
            .removeValue()
            .addOnSuccessListener { onCompleted.invoke(null) }
            .addOnFailureListener { onCompleted.invoke(it.message ?: "Error") }
    }

    private fun getImageFile(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        return formatter.format(Date())
    }
}