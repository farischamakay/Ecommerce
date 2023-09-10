package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerce.R
import com.example.ecommerce.adapter.ImageDetailAdapter
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.database.wishlist.Wishlist
import com.example.ecommerce.data.models.response.ProductDetailData
import com.example.ecommerce.databinding.FragmentDetailProductBinding
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var priceSum: Int = 0
    private lateinit var data: ProductDetailData
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by activityViewModels()
    private lateinit var indicatorContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString("id").toString()
        var isSettingChecked = false

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

                    setupIndicators(result.data.data.image)
                    setCurrentIndicator(0)

                    binding.apply {
                        progressBar.visibility = View.GONE
                        txtDetailHargaProduk.text = data.productPrice?.convertToRupiah()
                        txtTitleProduct.text = data.productName.toString()
                        txtProductTerjual.text = "Terjual ${data.sale.toString()}"
                        txtJumlahReview.text = "${data.productRating} (${data.totalReview})"
                        txtDeskripsiProduk.text = data.description
                        txtTotalStar.text = data.productRating.toString()
                        txtSatisfication.text = "${data.totalSatisfaction} % pembeli merasa puas."
                        txtUlasanRate.text = "${data.totalRating} Rating . ${data.totalReview} Review"
                    }

                    binding.btnFavorite.setOnCheckedChangeListener { _, isChecked ->
                        if (!isSettingChecked) {
                            isSettingChecked = true
                            if (isChecked) {
                                val dataNew = data.copy(productPrice = priceSum)
                                viewModel.insertToWishlist(convertToWishlist(dataNew))
                                Snackbar.make(
                                    view, "Product berhasil ditambahkan pada wishlist!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } else {
                                viewModel.deleteItemById(productId)
                                Snackbar.make(
                                    view, "Product dihapus dari wishlist!",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                            isSettingChecked = false
                        }
                    }

                    val adapter = data.image?.let { ImageDetailAdapter(it) }
                    binding.viewpagerImage.adapter = adapter
                    binding.viewpagerImage.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            setCurrentIndicator(position)
                        }
                    })

                    var counter = 0
                    data.productVariant.map {
                        createChips(it.variantName, counter++)
                    }

                    binding.chipVarianGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                        if(binding.chipVarianGroup.checkedChipId != View.NO_ID) {
                            val a = binding.chipVarianGroup.checkedChipId
                            val price = data.productVariant[a].variantPrice
                            if (data.productPrice != null)
                                priceSum = data.productPrice!! + price
                            binding.txtDetailHargaProduk.text = priceSum.convertToRupiah()
                        }

                    }

                    if (binding.chipVarianGroup.checkedChipId == View.NO_ID) {
                        (binding.chipVarianGroup.getChildAt(0) as Chip).isChecked = true
                    }

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

        viewModel.getDataWishlist.observe(viewLifecycleOwner) { response ->
            val isProductInWishlist = response.any { it.productId == productId }
            binding.btnFavorite.isChecked = isProductInWishlist
        }


        viewModel.getDataRoom.observe(viewLifecycleOwner) { response ->

            binding.btnTambahKeranjang.setOnClickListener {
                val cartData = response.find { it.productId == productId }
                if (cartData == null) {
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

    private fun convertToWishlist(detailData: ProductDetailData): Wishlist {
        return Wishlist(
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
            detailData.productRating.toString(),
            detailData.totalSatisfaction,
            detailData.productVariant[0].variantName,
            detailData.productPrice ?: 0
        )
    }

    private fun setupIndicators(data: List<String?>?) {
        if ((data?.size ?: 0) > 1) {
            indicatorContainer = binding.indicatorsContainer
            val indicator = arrayOfNulls<ImageView>(data!!.size)
            val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(8, 0, 8, 0)
            for (i in indicator.indices) {
                indicator[i] = ImageView(requireContext())
                indicator[i]?.let {
                    it.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.shape_indicator_inactive
                        )
                    )
                    it.layoutParams = layoutParams
                    indicatorContainer.addView(it)
                }
            }
        } else {
            binding.indicatorsContainer.visibility = View.GONE
        }
    }


    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_indicator_inactive
                    )
                )
            }
        }
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