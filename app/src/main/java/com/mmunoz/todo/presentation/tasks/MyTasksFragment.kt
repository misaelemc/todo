package com.mmunoz.todo.presentation.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.DESCRIPTION_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.IMAGES_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.NAME_FIELD
import com.mmunoz.todo.data.repositories.TaskRepositoryImpl.Companion.TASKS_REFERENCE
import com.mmunoz.todo.databinding.FragmentMyTasksBinding
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.domain.models.TaskArgs
import com.mmunoz.todo.domain.models.TaskModel
import com.mmunoz.todo.presentation.tasks.adapters.MyTaskAdapter
import com.mmunoz.todo.utils.showToastError
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MyTasksFragment : Fragment(), MyTaskAdapter.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TaskViewModel

    private var _binding: FragmentMyTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MyTaskAdapter

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

    override fun delete(key: String, images: List<String>) {
        viewModel.delete(key, images)
    }

    override fun navigateToTaskCreator(args: TaskArgs?) {
        findNavController().navigate(MyTasksFragmentDirections.actionMyTasksToCreator(args))
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskViewModel::class.java)
        viewModel.taskState.observe(viewLifecycleOwner, { response ->
            if (response is Response.Error) {
                showToastError(response.message)
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        fetchData()
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchData() {
        val query = FirebaseDatabase.getInstance()
            .getReference(TASKS_REFERENCE)

        val options = FirebaseRecyclerOptions.Builder<TaskModel>()
            .setQuery(query) {
                return@setQuery TaskModel(
                    it.child(NAME_FIELD).value.toString(),
                    it.child(IMAGES_FIELD).value as? List<String>,
                    it.child(DESCRIPTION_FIELD).value.toString()
                )
            }
            .setLifecycleOwner(viewLifecycleOwner)
            .build()
        adapter = MyTaskAdapter(requireContext(), this, options)
        binding.recyclerView.adapter = adapter
    }
}