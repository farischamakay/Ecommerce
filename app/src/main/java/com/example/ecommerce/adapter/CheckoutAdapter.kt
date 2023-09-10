package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.models.request.CheckoutRequest
import com.example.ecommerce.databinding.ItemListCheckoutBinding
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar

class CheckoutAdapter : ListAdapter<CheckoutRequest, CheckoutAdapter.CheckoutViewHolder>(CheckoutDiffCallback()) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.CheckoutViewHolder {
        val binding = ItemListCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckoutViewHolder, position: Int) {
        val checkoutItem = getItem(position)
        holder.bind(checkoutItem)
    }
    inner class CheckoutViewHolder(val binding : ItemListCheckoutBinding) :
            RecyclerView.ViewHolder(binding.root){
                fun bind(data : CheckoutRequest) {
                    Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
                    binding.txtTitleProduct.text = data.productName
                    binding.txtGigaByte.text = data.productVariant
                    binding.txtQuantity.text = data.quantity.toString()
                    binding.txtJumlahSisa.text = "Sisa ${data.stock}"
                    binding.txtHargaProduk.text = data.productVariantPrice.convertToRupiah()
                    binding.btnKurang.setOnClickListener {
                        data.quantity -= 1
                        if(data.quantity > 0){
                            binding.txtQuantity.text = data.quantity.toString()
                            onItemClickCallback.counterClicked(data)
                        }
                    }
                    binding.btnTambahQty.setOnClickListener {
                        if (data.quantity < (data.stock ?: 0)) {
                            data.quantity += 1
                            binding.txtQuantity.text = data.quantity.toString()
                            onItemClickCallback.counterClicked(data)
                        } else {
                            Snackbar.make(
                                binding.root, "Stok tidak mencukupi!",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
    interface OnItemClickCallback {
        fun counterClicked(checkout: CheckoutRequest)
    }
}
private class CheckoutDiffCallback : DiffUtil.ItemCallback<CheckoutRequest>() {
    override fun areItemsTheSame(oldItem: CheckoutRequest, newItem: CheckoutRequest): Boolean {
        return oldItem.productId == newItem.productId
    }
    override fun areContentsTheSame(oldItem: CheckoutRequest, newItem: CheckoutRequest): Boolean {
        return oldItem == newItem
    }
}