package com.mmunoz.todo.presentation.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.mmunoz.todo.R
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.DESCRIPTION_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.IMAGE_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.NAME_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.TASKS_REFERENCE
import com.mmunoz.todo.domain.models.TaskArgs
import com.mmunoz.todo.domain.models.TaskModel
import com.mmunoz.todo.databinding.FragmentMyTasksBinding
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.utils.showToastError
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MyTasksFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TaskViewModel

    private var _binding: FragmentMyTasksBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        binding.floatingButtonAdd.setOnClickListener {
            navigateToTaskCreator()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskViewModel::class.java)
        viewModel.taskErrorState.observe(viewLifecycleOwner, { error ->
            showToastError(getString(error))
        })
        viewModel.taskState.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Error -> showToastError(response.message)
                else -> Unit
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        fetchData()
    }

    private fun fetchData() {
        val query = FirebaseDatabase.getInstance()
            .getReference(TASKS_REFERENCE)

        val options = FirebaseRecyclerOptions.Builder<TaskModel>()
            .setQuery(query) {
                return@setQuery TaskModel(
                    it.child(NAME_FIELD).value.toString(),
                    it.child(DESCRIPTION_FIELD).value.toString(),
                    it.child(IMAGE_FIELD).value.toString()
                )
            }
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
        setupAdapter(options)
    }

    private fun setupAdapter(options: FirebaseRecyclerOptions<TaskModel>) {
        binding.recyclerView.adapter =
            object : FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>(options) {
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
                        val popupMenu = PopupMenu(requireContext(), it)
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
            }
    }

    private fun FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>.editTask(
        position: Int,
        task: TaskModel
    ) {
        getRef(position).key?.let { key ->
            navigateToTaskCreator(
                TaskArgs(
                    key,
                    task.name,
                    task.description,
                    task.image
                )
            )
        }
    }

    private fun FirebaseRecyclerAdapter<TaskModel, TaskViewHolder>.deleteTask(
        position: Int,
        task: TaskModel,
        options: FirebaseRecyclerOptions<TaskModel>
    ) {
        val newPosition = if (position >= options.snapshots.size) {
            position - 1
        } else {
            position
        }
        getRef(newPosition).key?.let { viewModel.delete(it, task.image) }
    }

    private fun navigateToTaskCreator(tasksArgs: TaskArgs? = null) {
        findNavController().navigate(MyTasksFragmentDirections.actionMyTasksToCreator(tasksArgs))
    }
}