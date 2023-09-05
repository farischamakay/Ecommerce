package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.adapter.ImageDetailAdapter
import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.data.models.response.ProductDetailData
import com.example.ecommerce.databinding.FragmentDetailProductBinding
import com.example.ecommerce.utils.ResourcesResult
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StoreViewModel by activityViewModels()

    private var priceSum: Int = 0
    private var productVarianName: String? = null
    private lateinit var data: ProductDetailData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
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
            when (result) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResourcesResult.Success -> {
                    data = result.data?.data!!
                    binding.apply {
                        progressBar.visibility = View.GONE
                        txtDetailHargaProduk.text = "Rp. ${data?.productPrice.toString()}"
                        txtTitleProduct.text = data?.productName.toString()
                        txtProductTerjual.text = "Terjual ${data?.sale.toString()}"
                        txtJumlahReview.text = "${data?.productRating} (${data?.totalReview})"
                        txtDeskripsiProduk.text = "${data?.description}"
                        txtTotalStar.text = "${data?.productRating}"
                        txtSatisfication.text = "${data?.totalSatisfaction} % pembeli merasa puas"
                        txtUlasanRate.text =
                            "${data?.totalRating} rating . ${data?.totalReview} Ulasan"
                    }

                    val adapter = data.image?.let { ImageDetailAdapter(it) }
                    binding.viewpagerImage.adapter = adapter

                    var counter = 0
                    data.productVariant.map {
                        createChips(it.variantName, counter++)
                    }

                    binding.chipVarianGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                        binding.chipVarianGroup.checkedChipId
                        val a = binding.chipVarianGroup.checkedChipId
                        val productName = data.productVariant[a].variantName
                        val price = data.productVariant[a].variantPrice
                        if (data.productPrice != null)
                            priceSum = data.productPrice!! + price
                        binding.txtDetailHargaProduk.text = "Rp. ${priceSum}"

                    }

                    (binding.chipVarianGroup.getChildAt(0) as Chip).isSelected = true


                    binding.btnAllReviews.setOnClickListener {
                        findNavController().navigate(
                            DetailProductFragmentDirections.actionDetailProductFragmentToReviewFragment(
                                productId
                            )
                        )
                    }
                }

                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.root.visibility = View.VISIBLE
                }

                else -> {}
            }
        }


        viewModel.getDataRoom.observe(viewLifecycleOwner) { response ->

            binding.btnTambahKeranjang.setOnClickListener {

                var cartData = response.find { it.productId == productId }

                if(cartData == null){
                    val dataNew = data.copy(productPrice = priceSum)
                    viewModel.insertToRoom(convertToCart(dataNew))
                    Snackbar.make(
                        view, "Product berhasil ditambahkan pada keranjang!",
                        Snackbar.LENGTH_LONG
                    ).show()

                } else {
                    var qtyCart = cartData.quantity

                        if (qtyCart < (data.stock ?: 0)) {
                            qtyCart += 1
                            viewModel.updateQuantity(listOf(convertToCart(data) to qtyCart))

                            Snackbar.make(
                                view, "Product berhasil ditambahkan pada keranjang!",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            Snackbar.make(
                                view, "Stok tidak tersedia",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
    }

    private fun convertToCart(detailData: ProductDetailData): Cart {
        return Cart(
            detailData.productId ?: "",
            detailData.productName,
            detailData.image?.get(0),
            detailData.brand,
            detailData.description,
            detailData.store,
            detailData.sale.toString(),
            detailData.stock,
            detailData.totalRating,
            detailData.totalReview,
            detailData.totalSatisfaction,
            detailData.productVariant[0].variantName,
            detailData.productPrice ?: 0
        )
    }

    private fun createChips(name: String?, varianId: Int) {
        val chip = Chip(requireContext())
        chip.apply {
            text = name
            id = varianId
            isCheckable = true
            binding.apply {
                if (name != null && name != "")
                    chipVarianGroup.addView(chip as View)
            }
        }
    }
}