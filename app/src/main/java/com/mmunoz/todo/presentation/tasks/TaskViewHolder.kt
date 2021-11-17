package com.mmunoz.todo.presentation.tasks

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mmunoz.todo.R

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textViewName: TextView = itemView.findViewById(R.id.textView_name)
    val imageButtonAction: AppCompatImageButton = itemView.findViewById(R.id.imageButton_action)
}