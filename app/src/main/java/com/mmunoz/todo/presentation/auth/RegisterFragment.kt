package com.mmunoz.todo.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mmunoz.todo.R
import com.mmunoz.todo.databinding.FragmentRegisterBinding
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.utils.show
import com.mmunoz.todo.utils.showErrorMessage
import com.mmunoz.todo.utils.showToastError
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AuthViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authState.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Error -> {
                    hideLoader()
                    showToastError(response.message)
                }
                is Response.Success -> {
                    hideLoader()
                    findNavController().navigate(R.id.action_register_to_my_tasks)
                }
                is Response.Loading -> {
                    binding.buttonCreate.isEnabled = false
                    binding.layoutLoader.show(true)
                }
            }
        })
        viewModel.authErrorState.observe(viewLifecycleOwner, { error ->
            binding.textViewError.showErrorMessage(getString(error))
        })
    }

    private fun hideLoader() {
        binding.buttonCreate.isEnabled = true
        binding.layoutLoader.show(false)
    }
}