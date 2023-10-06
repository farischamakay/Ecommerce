package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.core.data.models.response.ItemsItem
import com.example.ecommerce.databinding.ItemListGridLayoutBinding
import com.example.ecommerce.databinding.ItemListProdukBinding
import com.example.ecommerce.utils.convertToRupiah

class ProductListAdapter(private val onProductClick: (ItemsItem?) -> Unit) :
    PagingDataAdapter<ItemsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var isGrid: Boolean = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            when (holder) {
                is ListViewHolder -> holder.bind(data)

                is GridViewHolder -> holder.gridBind(data)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = when (viewType) {
            LIST_GRID -> ItemListGridLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            LIST_VIEW -> ItemListProdukBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            else -> throw IllegalArgumentException("Undefined view type")
        }

        return when (viewType) {
            LIST_GRID -> {
                val gridViewHolder =
                    GridViewHolder(binding as ItemListGridLayoutBinding, onProductClick)
                gridViewHolder
            }

            LIST_VIEW -> {
                val listViewHolder =
                    ListViewHolder(binding as ItemListProdukBinding, onProductClick)
                listViewHolder
            }

            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    class ListViewHolder(
        private val binding: ItemListProdukBinding,
        val onProductClick: (ItemsItem?) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemsItem) {
            Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
            binding.txtTitleProduct.text = data.productName
            binding.root.context.apply {
                binding.txtHargaProduk.text = data.productPrice?.convertToRupiah()
                binding.txtJumlahTerjual.text = getString(R.string.terjual, data.sale.toString())
            }
            binding.txtPemilikStore.text = data.brand
            binding.txtRatingProduct.text = data.productRating.toString()


            itemView.setOnClickListener {
                onProductClick(data)
            }
        }
    }

    class GridViewHolder(
        private val binding: ItemListGridLayoutBinding,
        val onProductClick: (ItemsItem?) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun gridBind(data: ItemsItem) {
            Glide.with(binding.root).load(data.image).into(binding.imgProduct)
            binding.txtTitleProduct.text = data.productName
            binding.txtProductBrand.text = data.brand
            binding.root.context.apply {
                binding.txtHargaProduct.text = data.productPrice?.convertToRupiah()
                binding.txtProductTerjual.text = getString(R.string.terjual, data.sale.toString())
            }
            itemView.setOnClickListener {
                onProductClick(data)
            }
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
