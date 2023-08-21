package com.example.ecommerce.prelogin.register

import android.os.Bundle
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
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding ?= null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator()

        binding.btnToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_profileFragment2)
//            val email = binding.inputEmailRegister.text.toString()
//            val password = binding.inputPasswordRegister.text.toString()
//            val userRequest = UserRequest(email = email, password = password)
//
//            viewModel.registerUser(userRequest)
//
//            viewModel.registerResult.observe(viewLifecycleOwner) { result ->
//                when(result){
//                    is ResourcesResult.Success -> {
//                        binding.progressbar.visibility = View.INVISIBLE
//                        binding.btnToProfile.visibility = View.VISIBLE
//                        Toast.makeText(requireContext(), "Berhasil membuat akun",
//                            Toast.LENGTH_LONG).show()
//                        findNavController().navigate(R.id.action_registerFragment_to_profileFragment2)
//
//                    }
//                    is ResourcesResult.Loading -> {
//                        binding.btnToProfile.visibility = View.INVISIBLE
//                        binding.progressbar.visibility = View.VISIBLE
//                    }
//                    is ResourcesResult.Failure -> {
//                        binding.progressbar.visibility = View.INVISIBLE
//                        binding.btnToProfile.visibility = View.VISIBLE
//                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
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