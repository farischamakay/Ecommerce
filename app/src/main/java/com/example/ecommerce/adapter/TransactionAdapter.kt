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
import com.example.ecommerce.utils.convertToRupiah

class TransactionAdapter : ListAdapter<TransactionDataItem, 
        TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnUlasClickListener(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
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
                binding.txtTotalPrice.text = data.total?.convertToRupiah()

                if (data.review == null ){
                    binding.btnUlas.visibility = View.VISIBLE
                    binding.btnUlas.setOnClickListener {
                        onItemClickCallback.onUlasClicked(data)
                    }
                }
            }

        }

    interface OnItemClickCallback{
        fun onUlasClicked (transactionDataItem: TransactionDataItem)
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