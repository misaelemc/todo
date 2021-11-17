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
import com.mmunoz.todo.databinding.FragmentLoginBinding
import com.mmunoz.todo.domain.models.Response
import com.mmunoz.todo.utils.show
import com.mmunoz.todo.utils.showErrorMessage
import com.mmunoz.todo.utils.showToastError
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
        binding.viewModel = viewModel
        viewModel.authState.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Error -> {
                    binding.layoutLoader.show(false)
                    showToastError(response.message)
                }
                is Response.Success -> {
                    binding.layoutLoader.show(false)
                    findNavController().navigate(R.id.action_login_to_my_tasks)
                }
                is Response.Loading -> binding.layoutLoader.show(true)
            }
        })
        viewModel.authErrorState.observe(viewLifecycleOwner, { error ->
            binding.textViewError.showErrorMessage(getString(error))
        })
    }
}