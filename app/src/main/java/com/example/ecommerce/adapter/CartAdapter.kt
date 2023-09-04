package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.databinding.ItemListCartBinding

class CartAdapter : ListAdapter<Cart, CartAdapter.CartViewHolder>(CartDiffCallback()){

    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class CartViewHolder(val binding : ItemListCartBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(data: Cart){
            Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
            binding.txtTitleProduct.text = data.productName
            binding.txtGigaByte.text = data.productVariant
            binding.txtJumlahSisa.text = "Sisa ${data.stock.toString()}"
            binding.txtHargaProduk.text = "Rp. ${data.productVariantPrice}"
            binding.btnKurang.setOnClickListener {  }
            binding.btnCekbox.setOnClickListener {
                val isChecked = !data.isCheck
                onItemClickCallback.onItemClicked(listOf(data to isChecked))

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)

        holder.binding.btnCekbox.isChecked = cartItem.isCheck

    }

    interface OnItemClickCallback {
        fun onItemClicked(cart: List<Pair<Cart, Boolean>>)
    }
}

private class CartDiffCallback : DiffUtil.ItemCallback<Cart>(){
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

}