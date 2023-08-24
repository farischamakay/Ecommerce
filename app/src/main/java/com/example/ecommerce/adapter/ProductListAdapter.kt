package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.databinding.ItemListProdukBinding

class ProductListAdapter : PagingDataAdapter<ItemsItem, ProductListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemListProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(private val binding : ItemListProdukBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data : ItemsItem){
                Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
                binding.txtTitleProduct.text = data.productName
                binding.txtHargaProduk.text = data.productPrice.toString()
                binding.txtPemilikStore.text = data.brand
                binding.txtRatingProduct.text = data.productRating.toString()
            }
        }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>(){
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.productId == newItem.productId
            }
        }
    }
}