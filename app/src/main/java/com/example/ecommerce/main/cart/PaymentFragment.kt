package com.example.ecommerce.main.cart

import ParentPaymentAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentPaymentBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding : FragmentPaymentBinding ?= null
    private val binding get() = _binding!!
    private lateinit var parentAdapter : ParentPaymentAdapter
    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }
    private val viewModel : CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args : PaymentFragmentArgs by navArgs()

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            }

        parentAdapter = ParentPaymentAdapter { paymentItem ->
            navController.navigate(PaymentFragmentDirections.
            actionPaymentFragmentToCheckoutFragment(args.listCheckout,paymentItem.image.toString(),paymentItem.label.toString())
            )
        }

        binding.rvListTransfer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTransfer.adapter = parentAdapter

        viewModel.fetchPayment()

        viewModel.paymentResult.observe(viewLifecycleOwner) { response ->
            Log.d("Payment", response.toString())
            when(response) {
                is ResourcesResult.Loading -> {}
                is ResourcesResult.Failure -> {}
                is ResourcesResult.Success -> {
                    val paymentResponse = response.data
                    Log.d("Payment", paymentResponse?.data.toString())
                    paymentResponse?.data?.let {
                        parentAdapter.submitList(it)
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}