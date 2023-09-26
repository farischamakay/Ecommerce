package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ecommerce.R
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.databinding.FragmentStatusBinding
import com.example.ecommerce.utils.Constants
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StatusFragment : Fragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var ratingUser: Int? = null
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ratingPembayaran.setOnRatingBarChangeListener { _, rating, _ ->
            ratingUser = rating.toInt()
        }

        val args: StatusFragmentArgs by navArgs()
        binding.btnDonePayment.setOnClickListener {
            val userReview = binding.txtInputReview.text
            val userAllReview = RatingRequest(
                userReview.toString(), ratingUser,
                args.fulfillmentDetail.invoiceId
            )
            firebaseAnalytics.logEvent(Constants.BUTTON_CLICK){
                param(Constants.BUTTON_NAME, "btn_done_payment")
            }
            viewModel.rating(userAllReview)

        }


        viewModel.ratingResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }
                is ResourcesResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root, getString(R.string.terima_kasih),
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigateUp()
                }
            }
        }

        binding.apply {
            txtIdTransaksi.text = args.fulfillmentDetail.invoiceId
            txtTanggalTransaksi.text = args.fulfillmentDetail.date
            txtWaktuTransaksi.text = args.fulfillmentDetail.time
            txtMetodeTransaksi.text = args.fulfillmentDetail.payment
            txtTotalTransaksi.text = args.fulfillmentDetail.total.convertToRupiah()
            if (args.fulfillmentDetail.status) {
                txtStatusTransaksi.text = getString(R.string.berhasil)
            }
        }
    }
}