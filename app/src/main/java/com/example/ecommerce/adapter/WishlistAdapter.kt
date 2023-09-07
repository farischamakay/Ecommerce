package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.database.wishlist.Wishlist
import com.example.ecommerce.databinding.ItemWishlistGridBinding
import com.example.ecommerce.databinding.ItemWishlistLinearBinding

class WishlistAdapter : ListAdapter<Wishlist, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var isGrid: Boolean = false
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) {
            LIST_GRID
        } else {
            LIST_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = when (viewType) {
            LIST_GRID -> {
                ItemWishlistGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            LIST_VIEW -> {
                ItemWishlistLinearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }

        return if (viewType == LIST_GRID) {
            WishListGridViewHolder(binding as ItemWishlistGridBinding)
        } else {
            WishListLinearViewHolder(binding as ItemWishlistLinearBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val cartItem = getItem(position)

        when (viewType) {
            LIST_GRID -> {
                val gridViewHolder = holder as WishListGridViewHolder
                gridViewHolder.bind(cartItem)
            }
            LIST_VIEW -> {
                val listViewHolder = holder as WishListLinearViewHolder
                listViewHolder.bind(cartItem)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class WishListLinearViewHolder(private val binding: ItemWishlistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Wishlist) {
            Glide.with(binding.root).load(data.image).into(binding.imgProductView)
            binding.txtTitleProduct.text = data.productName
            binding.txtHargaProduct.text = "Rp. ${data.productVariantPrice}"
            binding.txtStoreName.text = data.store
            binding.txtProductRating.text = data.productRating
            binding.txtProductTerjual.text = "| Terjual ${data.sale}"
            binding.btnDeleteItem.setOnClickListener {
                onItemClickCallback.onDeleteClicked(data.productId)
            }
            binding.btnTambahKeranjang.setOnClickListener {
                onItemClickCallback.onAddCartClicked(data)
            }
        }
    }

    inner class WishListGridViewHolder(private val binding: ItemWishlistGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Wishlist) {
            Glide.with(binding.root).load(data.image).into(binding.imgProduct)
            binding.txtTitleProduct.text = data.productName
            binding.txtHargaProduct.text = "Rp. ${data.productVariantPrice}"
            binding.txtProductBrand.text = data.brand
            binding.txtRatingProduct.text = data.totalRating.toString()
            binding.txtProductTerjual.text = "| Terjual ${data.sale}"
            binding.btnDeleteItem.setOnClickListener {
                onItemClickCallback.onDeleteClicked(data.productId)
            }
        }
    }

    interface OnItemClickCallback {
        fun onAddCartClicked(wishlist: Wishlist)
        fun onDeleteClicked(itemId  : String)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Wishlist>() {
            override fun areContentsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areItemsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
                return oldItem == newItem
            }
        }

        private const val LIST_VIEW = 0
        private const val LIST_GRID = 1
    }
}

