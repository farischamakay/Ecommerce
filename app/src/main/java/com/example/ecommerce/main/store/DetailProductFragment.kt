package com.example.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.adapter.ImageDetailAdapter
import com.example.ecommerce.databinding.FragmentDetailProductBinding
import com.example.ecommerce.utils.ResourcesResult
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding : FragmentDetailProductBinding ?= null
    private val binding get() = _binding!!

    private val viewModel : StoreViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString("id").toString()

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.detailItem(productId)



        viewModel.detailProduct.observe(viewLifecycleOwner) { result ->
            when(result){
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResourcesResult.Success -> {
                    val data = result.data?.data
                    binding.apply {
                        progressBar.visibility = View.GONE
                        txtDetailHargaProduk.text = "Rp. ${data?.productPrice.toString()}"
                        txtTitleProduct.text = data?.productName.toString()
                        txtProductTerjual.text = "Terjual ${data?.sale.toString()}"
                        txtJumlahReview.text = "${data?.productRating} (${data?.totalReview})"
                        txtDeskripsiProduk.text = "${data?.description}"
                        txtTotalStar.text = "${data?.productRating}"
                        txtSatisfication.text = "${data?.totalSatisfaction} % pembeli merasa puas"
                        txtUlasanRate.text = "${data?.totalRating} rating . ${data?.totalReview} Ulasan"
                    }

                    val adapter = data?.image?.let { ImageDetailAdapter(it) }
                    binding.viewpagerImage.adapter = adapter

                    var counter = 0
                    data?.productVariant?.map {
                        createChips(it?.variantName.toString(), counter++)
                    }

                    binding.chipVarianGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                        binding.chipVarianGroup.checkedChipId
                        val a = binding.chipVarianGroup.checkedChipId
                        Log.d("ChipId", a.toString())
                    }

                    binding.btnAllReviews.setOnClickListener {
                        findNavController().navigate(
                            DetailProductFragmentDirections.actionDetailProductFragmentToReviewFragment(productId))
                    }


                }
                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.root.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun createChips(name: String?, varianId : Int) {
        val chip = Chip(requireContext())
        chip.apply {
            text = name
            id  = varianId
            isCheckable = true
            binding.apply {
                if (name != null && name != "")
                    chipVarianGroup.addView(chip as View)
            }
        }
    }
}