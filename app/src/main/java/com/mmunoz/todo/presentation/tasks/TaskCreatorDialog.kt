package com.mmunoz.todo.presentation.tasks

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mmunoz.todo.databinding.DialogTaskCreatorBinding
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.presentation.tasks.adapters.PhotosAdapterController
import com.mmunoz.todo.utils.show
import com.mmunoz.todo.utils.showToastError
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TaskCreatorDialog : DialogFragment(), LocationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TaskViewModel

    private var _binding: DialogTaskCreatorBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val imageContentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data?.data != null && it.resultCode == Activity.RESULT_OK) {
                imageUri = it.data?.data
                viewModel.imageTask.value.add(imageUri!!)
                photosAdapterController.append(imageUri!!)
            }
        }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setLastKnownLocation()
        }
    }

    private lateinit var photosAdapterController: PhotosAdapterController
    private val args: TaskCreatorDialogArgs by navArgs()

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
        checkPermissions()
        setupViewModel()
        setupRecyclerView()
        viewModel.setArgs(args.task)
        binding.layoutImage.setOnClickListener { selectImage() }
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

    override fun onLocationChanged(location: Location) {
        viewModel.setLastKnownLocation(location)
        (requireActivity().getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
            ?.removeUpdates(this)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.taskState.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Error -> {
                    hideLoader()
                    showToastError(response.message)
                }
                is Response.Success -> {
                    hideLoader()
                    dismiss()
                }
                is Response.Loading -> {
                    binding.buttonSave.isEnabled = false
                    binding.layoutLoader.show(true)
                }
            }
        })
        viewModel.taskErrorState.observe(viewLifecycleOwner, { error ->
            showToastError(getString(error))
        })
        viewModel.taskImages.observe(viewLifecycleOwner, { images ->
            photosAdapterController.append(images.map { Uri.parse(it) })
        })
        viewModel.taskImageDeleted.observe(viewLifecycleOwner, { imageUri ->
            photosAdapterController.remove(imageUri)
        })
    }

    private fun hideLoader() {
        binding.buttonSave.isEnabled = true
        binding.layoutLoader.show(false)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            setLastKnownLocation()
        }
    }

    private fun setupRecyclerView() {
        photosAdapterController = PhotosAdapterController()
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.setController(photosAdapterController)
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
    }

    private fun selectImage() {
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            imageContentResult.launch(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLastKnownLocation() {
        (requireActivity().getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
            ?.requestLocationUpdates("gps", 0, 0F, this)
    }
}