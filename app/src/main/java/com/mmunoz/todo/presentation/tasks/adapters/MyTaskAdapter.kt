package com.mmunoz.todo.presentation.tasks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.mmunoz.todo.R
import com.mmunoz.todo.domain.models.TaskArgs
import com.mmunoz.todo.domain.models.TaskModel

class MyTaskAdapter constructor(
    private val context: Context,
    private val listener: Listener,
    private val options: FirebaseRecyclerOptions<TaskModel>
) : FirebaseRecyclerAdapter<TaskModel, MyTaskAdapter.TaskViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_view, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int,
        task: TaskModel
    ) {
        holder.textViewName.text = task.name
        holder.imageButtonAction.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> editTask(position, task)
                    R.id.action_delete -> deleteTask(position, task, options)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }

    private fun editTask(position: Int, task: TaskModel) {
        getRef(position).key?.let { key ->
            listener.navigateToTaskCreator(
                TaskArgs(
                    key,
                    task.name,
                    task.description,
                    task.images
                )
            )
        }
    }

    private fun deleteTask(
        position: Int,
        task: TaskModel,
        options: FirebaseRecyclerOptions<TaskModel>
    ) {
        val newPosition = if (position >= options.snapshots.size) {
            position - 1
        } else {
            position
        }
        getRef(newPosition).key?.let { listener.delete(it, task.images.orEmpty()) }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewName: TextView = itemView.findViewById(R.id.textView_name)
        val imageButtonAction: AppCompatImageButton = itemView.findViewById(R.id.imageButton_action)
    }

    interface Listener {
        fun delete(key: String, images: List<String>)
        fun navigateToTaskCreator(args: TaskArgs? = null)
    }
}