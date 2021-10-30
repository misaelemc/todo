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
import com.mmunoz.todo.databinding.FragmentLoginBinding
import com.mmunoz.todo.ui.helpers.showError
import com.mmunoz.todo.ui.helpers.showErrorMessage
import com.mmunoz.todo.ui.viewModels.AuthViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AuthViewModel

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.buttonLogin.setOnClickListener {
            val user = binding.editTextUser.text?.toString().orEmpty()
            val password = binding.editTextPassword.text?.toString().orEmpty()
            viewModel.login(user, password)
        }
        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_fragment)
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
                AuthAction.Success -> findNavController().navigate(R.id.action_login_to_my_tasks)
                is AuthAction.Error -> binding.textViewError.showError(action.message)
                is AuthAction.ErrorMessage -> binding.textViewError.showErrorMessage(action.message)
            }
        })
    }
}