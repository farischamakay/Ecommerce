package com.example.ecommerce.prelogin.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentLoginBinding
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            findNavController().navigate(R.id.prelogin_to_main)
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