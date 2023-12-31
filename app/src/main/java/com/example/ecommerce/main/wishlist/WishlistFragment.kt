package com.example.ecommerce.main.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.WishlistAdapter
import com.example.ecommerce.core.data.database.cart.Cart
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.databinding.FragmentWhistlistBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWhistlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WishlistViewModel by viewModels()
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhistlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishlistAdapter = WishlistAdapter()
        val gridManager = GridLayoutManager(requireActivity(), 1)
        binding.rvProductList.layoutManager = gridManager
        binding.rvProductList.adapter = wishlistAdapter

        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == wishlistAdapter.itemCount && wishlistAdapter.isGrid) {
                    2
                } else {
                    1
                }
            }
        }

        binding.btnChangeGrid.setOnCheckedChangeListener { _, isChecked ->
            wishlistAdapter.isGrid = isChecked
            if (isChecked) {
                gridManager.spanCount = 2
            } else {
                gridManager.spanCount = 1
            }
        }

        viewModel.getDataWishlist.observe(viewLifecycleOwner) { response ->
            wishlistAdapter.submitList(response)
            binding.txtJumlahWishlist.text = getString(R.string.barang, response.size.toString())
            binding.emptyState.root.isVisible = response.isNullOrEmpty()
        }

        viewModel.getDataRoom.observe(viewLifecycleOwner) { response ->

            wishlistAdapter.setOnItemClickCallback(object : WishlistAdapter.OnItemClickCallback {

                override fun onAddCartClicked(wishlist: Wishlist, itemId: String) {
                    val cartData = response.find { it.productId == itemId }
                    if (cartData == null) {
                        viewModel.insertToRoom(convertToCart(wishlist))
                        Snackbar.make(
                            view, getString(R.string.product_ditambahkan_pada_keranjang),
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        var qtyCart = cartData.quantity
                        if (qtyCart < (wishlist.stock ?: 0)) {
                            qtyCart += 1
                            viewModel.updateQuantity(listOf(convertToCart(wishlist) to qtyCart))
                            Snackbar.make(
                                view,
                                getString(R.string.product_berhasil_ditambahkan_pada_keranjang),
                                Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            Snackbar.make(
                                view, getString(R.string.stok_tidak_mencukupi),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onDeleteClicked(itemId: String) {
                    viewModel.deleteItemById(itemId)
                    Snackbar.make(
                        view, "Deleted item!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun convertToCart(detailData: Wishlist): Cart {
        return Cart(
            detailData.productId,
            detailData.productName,
            detailData.image,
            detailData.brand,
            detailData.description,
            detailData.store,
            detailData.sale.toString(),
            detailData.stock,
            detailData.totalRating,
            detailData.totalReview,
            detailData.totalSatisfaction,
            detailData.productVariant,
            detailData.productVariantPrice
        )
    }

}