package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.models.response.TransactionDataItem
import com.example.ecommerce.databinding.ItemListTransactionBinding

class TransactionAdapter : ListAdapter<TransactionDataItem, 
        TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.TransactionViewHolder {
        val binding = ItemListTransactionBinding.inflate(LayoutInflater.from(parent.context),
            parent, false )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionAdapter.TransactionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    inner class TransactionViewHolder(val binding : ItemListTransactionBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(data : TransactionDataItem) {
                Glide.with(binding.root).load(data.image).into(binding.imgProduct)
                binding.txtTanggalPembelian.text = data.date
                binding.txtNumberOfProduct.text = "${data.items?.map { it?.quantity }} Barang"
                binding.txtTotalPrice.text = data.total.toString()

                if (data.items!!.isNotEmpty()){
                    binding.btnUlas.visibility = View.VISIBLE
                }
            }

        }
}

private class TransactionDiffCallback : DiffUtil.ItemCallback<TransactionDataItem>() {
    override fun areItemsTheSame(oldItem: TransactionDataItem, newItem: TransactionDataItem): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: TransactionDataItem, newItem: TransactionDataItem): Boolean {
        return oldItem == newItem
    }
}