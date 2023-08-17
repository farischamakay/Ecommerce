package com.example.ecommerce.main.whistlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentWhistlistBinding

class WhistlistFragment : Fragment() {

    private var _binding : FragmentWhistlistBinding ?= null
    private val  binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWhistlistBinding.inflate(layoutInflater)
        return binding.root
    }


}