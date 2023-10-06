package com.example.ecommerce.main.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.MainFragmentDirections
import com.example.ecommerce.R
import com.example.ecommerce.adapter.TransactionAdapter
import com.example.ecommerce.core.data.models.response.TransactionDataItem
import com.example.ecommerce.core.data.models.response.transactionToReview
import com.example.ecommerce.databinding.FragmentTransactionBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionAdapter = TransactionAdapter()
        binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransaction.adapter = transactionAdapter

        viewModel.fetchTransaction()

        transactionAdapter.setOnUlasClickListener(object :
            TransactionAdapter.OnItemClickCallback {
            override fun onUlasClicked(transactionDataItem: TransactionDataItem) {
                navController.navigate(
                    MainFragmentDirections.actionMainFragmentToStatusFragment(
                        transactionDataItem.transactionToReview()
                    )
                )
            }
        })

        viewModel.paymentResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutTransactionError.root.isVisible = true
                    binding.layoutTransactionError.txtErrorCode.text =
                        getString(R.string.empty_code)
                    binding.layoutTransactionError.txtMsgError.text =
                        getString(R.string.empty_error)
                    binding.layoutTransactionError.btnReset.setOnClickListener {
                        viewModel.fetchTransaction()
                    }
                }

                is ResourcesResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    transactionAdapter.submitList(response.data!!.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTransaction()
    }
}