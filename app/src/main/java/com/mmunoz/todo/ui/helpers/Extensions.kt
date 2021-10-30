package com.mmunoz.todo.ui.helpers

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun TextView.showError(@StringRes error: Int) {
    showErrorMessage(context.getString(error))
}

fun TextView.showErrorMessage(error: String) {
    text = error
    visibility = View.VISIBLE
    Handler(Looper.getMainLooper()).postDelayed({
        visibility = View.GONE
    }, 5000)
}

fun Fragment.showToastError(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}