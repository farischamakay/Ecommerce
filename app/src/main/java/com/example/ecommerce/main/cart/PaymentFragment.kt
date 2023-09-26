package com.example.ecommerce.main.cart

import ParentPaymentAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentPaymentBinding
import com.example.ecommerce.utils.ResourcesResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var parentAdapter: ParentPaymentAdapter
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics
    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: PaymentFragmentArgs by navArgs()

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        parentAdapter = ParentPaymentAdapter { paymentItem ->
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO){
                param(FirebaseAnalytics.Param.PAYMENT_TYPE, paymentItem.label.toString())
            }
            navController.navigate(
                PaymentFragmentDirections.actionPaymentFragmentToCheckoutFragment(
                    args.listCheckout,
                    paymentItem.image.toString(),
                    paymentItem.label.toString()
                )
            )
        }

        binding.rvListTransfer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTransfer.adapter = parentAdapter

        viewModel.paymentResult.observe(viewLifecycleOwner) { response ->
            Log.d("Payment", response.toString())
            when (response) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is ResourcesResult.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
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