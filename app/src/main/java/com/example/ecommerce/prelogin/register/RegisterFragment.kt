package com.example.ecommerce.prelogin.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding ?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator()

        binding.btnToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_profileFragment2)
        }

        binding.btnMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun validator(){
        val elr = binding.layoutEmailRegister
        val edr = binding.inputEmailRegister
        val plr = binding.layoutPasswordRegister
        val pdr = binding.inputPasswordRegister
        val btnRegister = binding.btnToProfile

        elr.editText?.doOnTextChanged{
                inputEmail, _, _, _ ->
            if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                elr.error = "Email tidak valid"
                btnRegister.isEnabled = false
            } else {
                elr.error = ""
                updateButtonState(edr, pdr, btnRegister)
            }
        }

        plr.editText?.doOnTextChanged(){
                inputPassword,_,_,_ ->
            if(inputPassword!!.length < 8){
                plr.error = "Password tidak valid"
                btnRegister.isEnabled = false
            } else {
                plr.error = ""
                updateButtonState(edr, pdr, btnRegister)
            }
        }

    }

    private fun updateButtonState(emailEditText: EditText, passwordEditText: EditText, button: Button) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
        val isPasswordValid = passwordEditText.text.length >= 8

        binding.btnToProfile.isEnabled = isEmailValid && isPasswordValid

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}