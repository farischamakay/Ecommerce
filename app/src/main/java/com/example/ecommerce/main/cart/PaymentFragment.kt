package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.ParentPaymentAdapter
import com.example.ecommerce.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private var _binding : FragmentPaymentBinding ?= null
    private val binding get() = _binding!!
    private lateinit var parentAdapter : ParentPaymentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentAdapter = ParentPaymentAdapter()
        binding.rvListTransfer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTransfer.adapter = parentAdapter

//        parentAdapter.submitList()

    }


}