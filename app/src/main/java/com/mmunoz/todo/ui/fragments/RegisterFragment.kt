package com.mmunoz.todo.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mmunoz.todo.R
import com.mmunoz.todo.data.models.AuthAction
import com.mmunoz.todo.databinding.FragmentRegisterBinding
import com.mmunoz.todo.ui.helpers.showError
import com.mmunoz.todo.ui.helpers.showErrorMessage
import com.mmunoz.todo.ui.viewModels.AuthViewModel
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
        binding.buttonCreate.setOnClickListener {
            val user = binding.editTextUser.text?.toString().orEmpty()
            val password = binding.editTextPassword.text?.toString().orEmpty()
            val cPassword = binding.editTextConfirmPassword.text?.toString().orEmpty()
            viewModel.register(user, password, cPassword)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AuthViewModel::class.java)
        viewModel.liveData.observe(viewLifecycleOwner, { action ->
            when (action) {
                AuthAction.Success -> findNavController().navigate(R.id.action_register_to_my_tasks)
                is AuthAction.Error -> binding.textViewError.showError(action.message)
                is AuthAction.ErrorMessage -> binding.textViewError.showErrorMessage(action.message)
            }
        })
    }
}