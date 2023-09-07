package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.CheckoutAdapter
import com.example.ecommerce.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private var _binding : FragmentCheckoutBinding ?=  null
    private lateinit var checkboxAdapter : CheckoutAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args : CheckoutFragmentArgs by navArgs()

        checkboxAdapter = CheckoutAdapter()
        binding.rvCheckout.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCheckout.adapter = checkboxAdapter

        checkboxAdapter.submitList(args.listCheckout.listCheckout)


        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnNextPayment.setOnClickListener {

        }
    }
}