package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.databinding.ItemListGridLayoutBinding
import com.example.ecommerce.databinding.ItemListProdukBinding

class ProductListAdapter : PagingDataAdapter<ItemsItem, ViewHolder>(DIFF_CALLBACK) {

    var isGrid: Boolean = false

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            when (holder) {
                is ListViewHolder -> {
                    holder.bind(data)
                }

                is GridViewHolder -> {
                    holder.gridBind(data)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) {
            LIST_GRID
        } else {
            LIST_VIEW
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        when (viewType) {
            LIST_GRID -> {
                val binding = ItemListGridLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return GridViewHolder(binding)
            }

            LIST_VIEW -> {
                val binding = ItemListProdukBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ListViewHolder(binding)
            }

            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    class ListViewHolder(private val binding: ItemListProdukBinding) :
        ViewHolder(binding.root) {
        fun bind(data: ItemsItem) {
            Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
            binding.txtTitleProduct.text = data.productName
            binding.txtHargaProduk.text = "Rp. ${data.productPrice}"
            binding.txtPemilikStore.text = data.brand
            binding.txtRatingProduct.text = data.productRating.toString()
            binding.txtJumlahTerjual.text = "Terjual ${data.sale}"
        }
    }

    class GridViewHolder(private val binding: ItemListGridLayoutBinding) :
        ViewHolder(binding.root) {
        fun gridBind(data: ItemsItem) {
            Glide.with(binding.root).load(data.image).into(binding.imgProduct)
            binding.txtTitleProduct.text = data.productName
            binding.txtHargaProduct.text = "Rp. ${data.productPrice}"
            binding.txtProductBrand.text = data.brand
            binding.txtRatingProduct.text = data.productRating.toString()
            binding.txtProductTerjual.text = "Terjual ${data.sale}"
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.productId == newItem.productId
            }
        }

        private const val LIST_VIEW = 0
        private const val LIST_GRID = 1
    }
}