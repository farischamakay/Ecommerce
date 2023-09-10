package com.example.ecommerce.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.TransactionAdapter
import com.example.ecommerce.databinding.FragmentTransactionBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchTransaction()

        transactionAdapter = TransactionAdapter()
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = transactionAdapter


        viewModel.paymentResult.observe(viewLifecycleOwner){response ->
            when(response){
                is ResourcesResult.Loading -> {

                }
                is ResourcesResult.Failure -> {

                }
                is ResourcesResult.Success -> {
                    transactionAdapter.submitList(response.data!!.data)
                }
            }
        }
    }

}