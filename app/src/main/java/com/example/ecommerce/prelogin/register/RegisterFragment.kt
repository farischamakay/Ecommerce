package com.example.ecommerce.prelogin.register

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
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
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.utils.Constants
import com.example.ecommerce.utils.ResourcesResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    private var token: String = ""

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
        })

        FirebaseMessaging.getInstance().subscribeToTopic("promo")
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Promo", "Fetching FCM registration promo failed", task.exception)
                    return@OnCompleteListener
                } else {
                    Log.d("Promo", "Fetching FCM registration promo success")
                }
            })

        binding.btnToProfile.setOnClickListener {
            val email = binding.inputEmailRegister.text.toString()
            val password = binding.inputPasswordRegister.text.toString()
            val userRequest = UserRequest(email = email, password = password, firebaseToken = token)

            viewModel.registerUser(userRequest)
        }

        binding.btnMasuk.setOnClickListener {
            firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                param(Constants.BUTTON_NAME, "btn_masuk")
            }
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResourcesResult.Success -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    binding.btnToProfile.visibility = View.VISIBLE

                    val accessToken = result.data.data?.accessToken
                    val refreshToken = result.data.data?.refreshToken

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                        param(FirebaseAnalytics.Param.METHOD, "email")
                    }

                    if (accessToken != null && refreshToken != null) {
                        viewModel.saveToken(accessToken, refreshToken)
                    }

                    Toast.makeText(
                        requireContext(), "Berhasil membuat akun",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_registerFragment_to_profileFragment2)

                }

                is ResourcesResult.Loading -> {
                    binding.btnToProfile.visibility = View.INVISIBLE
                    binding.progressbar.visibility = View.VISIBLE
                }

                is ResourcesResult.Failure -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    binding.btnToProfile.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun validator() {
        val elr = binding.layoutEmailRegister
        val edr = binding.inputEmailRegister
        val plr = binding.layoutPasswordRegister
        val pdr = binding.inputPasswordRegister
        val btnRegister = binding.btnToProfile

        elr.editText?.doOnTextChanged { inputEmail, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail ?: "")
                    .matches() && !inputEmail.isNullOrEmpty()
            ) {
                elr.error = getString(R.string.email_tidak_valid)
                btnRegister.isEnabled = false
            } else {
                elr.error = ""
                updateButtonState(edr, pdr, btnRegister)
            }
        }

        plr.editText?.doOnTextChanged { inputPassword, _, _, _ ->
            if ((inputPassword?.length ?: 0) < 8 && !inputPassword.isNullOrEmpty()) {
                plr.error = getString(R.string.password_tidak_valid)
                btnRegister.isEnabled = false
            } else {
                plr.error = ""
                updateButtonState(edr, pdr, btnRegister)
            }
        }

    }

    private fun updateButtonState(
        emailEditText: EditText,
        passwordEditText: EditText,
        button: Button
    ) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
        val isPasswordValid = passwordEditText.text.length >= 8

        binding.btnToProfile.isEnabled = isEmailValid && isPasswordValid

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}