package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {

    private var _binding : FragmentStatusBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

}