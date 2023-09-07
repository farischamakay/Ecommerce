package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.databinding.ItemListCheckoutBinding

class CheckoutAdapter : ListAdapter<Cart, CheckoutAdapter.CheckoutViewHolder>(CheckoutDiffCallback()) {
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
                fun bind(data : Cart) {
                    Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
                    binding.txtTitleProduct.text = data.productName
                    binding.txtGigaByte.text = data.productVariant
                    binding.txtQuantity.text = data.quantity.toString()
                    binding.txtJumlahSisa.text = "Sisa ${data.quantity}"
                    binding.txtHargaProduk.text = "Rp. ${data.productVariantPrice}"
                }
            }

}
private class CheckoutDiffCallback : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
}