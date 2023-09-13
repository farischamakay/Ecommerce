package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar

class CartAdapter : ListAdapter<Cart, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    interface OnItemClickCallback {
        fun onItemClicked(cart: List<Pair<Cart, Boolean>>)
        fun counterClicked(cart: List<Pair<Cart, Int>>)
        fun onDeleteClicked(itemId  : String)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class CartViewHolder(val binding: ItemListCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Cart) {
            Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
            binding.txtTitleProduct.text = data.productName
            binding.txtGigaByte.text = data.productVariant
            binding.root.context.apply {
                if((data.stock ?:0) < 10){
                    val red = MaterialColors.getColor(
                        binding.root.rootView, com.google.android.material.R.attr.colorError
                    )
                    binding.txtJumlahSisa.setTextColor(red)
                    binding.txtJumlahSisa.text = getString(R.string.sisa, data.stock.toString())
                } else {
                    binding.txtJumlahSisa.text = getString(R.string.sisa, data.stock.toString())
                }
                binding.txtHargaProduk.text = data.productVariantPrice.convertToRupiah()
            }
            binding.txtQuantity.text = data.quantity.toString()
            binding.btnKurang.setOnClickListener {
                var quantity = data.quantity
                quantity -= 1
                if(quantity > 0){
                    binding.txtQuantity.text = data.quantity.toString()
                    onItemClickCallback.counterClicked(listOf(data to quantity))
                }
            }
            binding.btnTambahQty.setOnClickListener {
                if (data.quantity < (data.stock ?: 0)) {
                    var quantity = data.quantity
                    quantity += 1
                    binding.txtQuantity.text = quantity.toString()
                    onItemClickCallback.counterClicked(listOf(data to quantity))
                } else {
                    Snackbar.make(
                        binding.root, "Stok tidak mencukupi!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            binding.imageButton.setOnClickListener {
                onItemClickCallback.onDeleteClicked(data.productId)
            }
            binding.btnCekbox.setOnClickListener {
                val isChecked = binding.btnCekbox.isChecked
                onItemClickCallback.onItemClicked(listOf(data to isChecked))
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
        holder.binding.txtQuantity.text = cartItem.quantity.toString()
        holder.binding.btnCekbox.isChecked = cartItem.isCheck
    }
}

private class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.productId == newItem.productId
    }
    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
}