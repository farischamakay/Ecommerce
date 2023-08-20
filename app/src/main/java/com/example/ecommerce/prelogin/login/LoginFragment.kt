package com.example.ecommerce.prelogin.login

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
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
            viewModel.loginUser(userRequest)

            viewModel.loginResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResourcesResult.Loading -> {
                        binding.btnToHome.visibility = View.INVISIBLE
                        binding.progressbar.visibility = View.VISIBLE
                    }

                    is ResourcesResult.Success -> {
                        val loginResponse = result.data
                        binding.progressbar.visibility = View.GONE
                        binding.btnToHome.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),"Login berhasil!", Toast.LENGTH_LONG).show()
                        Log.d("LOGINRES", loginResponse.toString())
                        // Handle successful login response
                    }

                    is ResourcesResult.Failure -> {
                        binding.progressbar.visibility = View.GONE
                        binding.btnToHome.visibility = View.VISIBLE
                        val errorMessage = result.error
                        Toast.makeText(requireContext(),errorMessage, Toast.LENGTH_LONG).show()
                        Log.d("LOGINERR", errorMessage.toString())
                        // Handle error
                    }
                }
            }
            //findNavController().navigate(R.id.prelogin_to_main)
        }
    }

    private fun validator(){
        val el = binding.layoutEmailLogin
        val et = binding.inputEmailLogin
        val pl = binding.layoutPasswordLogin
        val pt = binding.inputPasswordLogin
        val btnHome = binding.btnToHome

        el.editText?.doOnTextChanged{
            inputEmail, _, _, _ ->
            if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                el.error = "Email tidak valid"
                btnHome.isEnabled = false
            } else {
                el.error = ""
                updateButtonState(et, pt, btnHome)
            }
        }

        pl.editText?.doOnTextChanged(){
            inputPassword,_,_,_ ->
            if(inputPassword!!.length < 8){
                pl.error = "Password tidak valid"
                btnHome.isEnabled = false
            } else {
                pl.error = ""
                updateButtonState(et, pt, btnHome)
            }
        }

    }

    private fun updateButtonState(emailEditText: EditText, passwordEditText: EditText, button: Button) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
        val isPasswordValid = passwordEditText.text.length >= 8

        binding.btnToHome.isEnabled = isEmailValid && isPasswordValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}