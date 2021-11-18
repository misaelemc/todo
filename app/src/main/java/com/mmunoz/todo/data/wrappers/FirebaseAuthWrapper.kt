package com.mmunoz.todo.data.wrappers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthWrapper @Inject constructor() {

    fun signInWithEmailAndPassword(username: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
    }

    fun createUserWithEmailAndPassword(username: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
    }
}