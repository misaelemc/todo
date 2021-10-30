package com.mmunoz.todo.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mmunoz.todo.databinding.DialogTaskCreatorBinding
import com.mmunoz.todo.ui.helpers.showToastError
import com.mmunoz.todo.ui.viewModels.TaskViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TaskCreatorDialog : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TaskViewModel

    private var _binding: DialogTaskCreatorBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val imageContentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data
                binding.imageView.setImageURI(imageUri)
                binding.imageView.visibility = View.VISIBLE
                binding.imageViewIcon.visibility = View.GONE
                binding.textViewTitle.visibility = View.GONE
            }
        }

    val args: TaskCreatorDialogArgs by navArgs()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTaskCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        if (args.task != null) {
            binding.editTextNameTast.setText(args.task!!.name)
            binding.editTextDescription.setText(args.task!!.description)
        }
        binding.layoutImage.setOnClickListener { selectImage() }
        binding.buttonSave.setOnClickListener {
            binding.layoutLoader.visibility = View.VISIBLE
            if (args.task == null) {
                viewModel.addTask(
                    binding.editTextNameTast.text?.toString().orEmpty(),
                    binding.editTextDescription.text?.toString().orEmpty(),
                    imageUri
                )
            } else {
                viewModel.editTask(
                    args.task!!.key,
                    binding.editTextNameTast.text?.toString().orEmpty(),
                    binding.editTextDescription.text?.toString().orEmpty(),
                    args.task!!.image.orEmpty(),
                    imageUri
                )
            }
        }
    }

    override fun onResume() {
        requireDialog().window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        imageUri = null
        super.onDestroyView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, { action ->
            binding.layoutLoader.visibility = View.GONE
            when (action) {
                is TaskViewModel.TaskAction.Success -> dismiss()
                is TaskViewModel.TaskAction.Error -> showToastError(getString(action.message))
                is TaskViewModel.TaskAction.ErrorMessage -> showToastError(action.message)
            }
        })
    }

    private fun selectImage() {
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            imageContentResult.launch(this)
        }
    }
}