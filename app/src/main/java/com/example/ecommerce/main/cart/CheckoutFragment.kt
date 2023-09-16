package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.adapter.CheckoutAdapter
import com.example.ecommerce.data.models.request.CheckoutRequest
import com.example.ecommerce.data.models.request.FullfilmentItem
import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.response.fulfillmentToReview
import com.example.ecommerce.databinding.FragmentCheckoutBinding
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private lateinit var checkboxAdapter: CheckoutAdapter
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
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
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: CheckoutFragmentArgs by navArgs()

        val titlePembayaran = args.titlePayment
        val imagePembayaran = args.imagePayment

        if (titlePembayaran.isNotEmpty()) {
            Glide.with(binding.root).load(imagePembayaran).into(binding.imgCardView)
            binding.txtChosePayment.text = titlePembayaran
            binding.btnBayar.isEnabled = true
            binding.btnBayar.setOnClickListener {
                val productItems = args.listCheckout.listCheckout.map {
                    FullfilmentItem(
                        quantity = it.quantity,
                        productId = it.productId,
                        variantName = it.productVariant
                    )
                }
                val fulfillmentRequest = FullfilmentRequest(titlePembayaran, productItems.toList())

                viewModel.fulfillment(fulfillmentRequest)

                viewModel.fulfillmentResult.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is ResourcesResult.Loading -> {}
                        is ResourcesResult.Success -> {
                            val data = response.data?.data
                            Snackbar.make(
                                binding.root, "Pembayaran berhasil dilakukan!",
                                Snackbar.LENGTH_LONG
                            ).show()
                            if (data != null) {
                                navController.navigate(
                                    CheckoutFragmentDirections.actionCheckoutFragmentToStatusFragment(
                                        data.fulfillmentToReview()
                                    )
                                )
                            }
                        }

                        is ResourcesResult.Failure -> {}
                    }
                }
            }
        }

        checkboxAdapter = CheckoutAdapter()
        binding.rvCheckout.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCheckout.adapter = checkboxAdapter

        var priceItem = args.listCheckout.listCheckout.map { it.productVariantPrice * it.quantity }
        binding.txtTotalBayar.text = priceItem.sum().convertToRupiah()

        checkboxAdapter.setOnItemClickCallback(object : CheckoutAdapter.OnItemClickCallback {
            override fun counterClicked(checkout: CheckoutRequest) {
                priceItem =
                    args.listCheckout.listCheckout.map { it.productVariantPrice * it.quantity }
                val totalPrice = priceItem.sum()
                binding.txtTotalBayar.text = totalPrice.convertToRupiah()
            }
        })

        checkboxAdapter.submitList(args.listCheckout.listCheckout)

        binding.txtTitleTotalBayar.text
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnNextPayment.setOnClickListener {
            navController.navigate(
                CheckoutFragmentDirections.actionCheckoutFragmentToPaymentFragment(
                    args.listCheckout
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}