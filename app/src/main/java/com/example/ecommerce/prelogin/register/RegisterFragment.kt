package com.example.ecommerce.prelogin.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.btnToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_profileFragment2)
        }

        binding.btnMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}