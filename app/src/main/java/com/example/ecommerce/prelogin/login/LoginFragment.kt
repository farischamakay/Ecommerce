package com.example.ecommerce.prelogin.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator()

        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnToHome.setOnClickListener {
            val email = binding.inputEmailLogin.text.toString()
            val password = binding.inputPasswordLogin.text.toString()
            val userRequest = UserRequest(email = email, password = password)

            if (viewModel.getUsername().isNullOrEmpty())
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment2)
            else
                viewModel.loginUser(userRequest)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResourcesResult.Loading -> {
                    binding.btnToHome.visibility = View.INVISIBLE
                    binding.progressbar.visibility = View.VISIBLE
                }

                is ResourcesResult.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.btnToHome.visibility = View.VISIBLE

                    val accessToken = result.data.data?.accessToken
                    val refreshToken = result.data.data?.refreshToken
                    if (accessToken != null && refreshToken != null) {
                        viewModel.saveToken(accessToken,refreshToken)
                    }
                    Toast.makeText(requireContext(), "Login berhasil!", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is ResourcesResult.Failure -> {
                    binding.progressbar.visibility = View.GONE
                    binding.btnToHome.visibility = View.VISIBLE
                    val errorMessage = result.error
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun validator() {
        val el = binding.layoutEmailLogin
        val et = binding.inputEmailLogin
        val pl = binding.layoutPasswordLogin
        val pt = binding.inputPasswordLogin
        val btnHome = binding.btnToHome

        el.editText?.doOnTextChanged { inputEmail, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail ?: "").matches()
                && !inputEmail.isNullOrEmpty() ) {
                el.error = "Email tidak valid"
                btnHome.isEnabled = false
            } else {
                el.error = ""
                updateButtonState(et, pt, btnHome)
            }
        }

        pl.editText?.doOnTextChanged { inputPassword, _, _, _ ->
            if ((inputPassword?.length ?: 0) < 8 && !inputPassword.isNullOrEmpty()) {
                pl.error = "Password tidak valid"
                btnHome.isEnabled = false
            } else {
                pl.error = ""
                updateButtonState(et, pt, btnHome)
            }
        }

    }

    private fun updateButtonState(
        emailEditText: EditText,
        passwordEditText: EditText,
        btnHome: Button
    ) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
        val isPasswordValid = passwordEditText.text.length >= 8

        binding.btnToHome.isEnabled = isEmailValid && isPasswordValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}