package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemListCheckoutBinding
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar

class CheckoutAdapter :
    ListAdapter<com.example.ecommerce.core.data.models.request.CheckoutRequest, CheckoutAdapter.CheckoutViewHolder>(
        CheckoutDiffCallback()
    ) {

    interface OnItemClickCallback {
        fun counterClicked(checkout: com.example.ecommerce.core.data.models.request.CheckoutRequest)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.CheckoutViewHolder {
        val binding = ItemListCheckoutBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckoutViewHolder, position: Int) {
        val checkoutItem = getItem(position)
        holder.bind(checkoutItem)
    }

    inner class CheckoutViewHolder(val binding: ItemListCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: com.example.ecommerce.core.data.models.request.CheckoutRequest) {
            Glide.with(binding.root).load(data.image).into(binding.imgThumbnail)
            binding.txtTitleProduct.text = data.productName
            binding.txtGigaByte.text = data.productVariant
            binding.txtQuantity.text = data.quantity.toString()
            binding.txtJumlahSisa.text = binding.root.context.getString(
                R.string.sisa,
                data.stock.toString()
            )//"Sisa ${data.stock}"
            binding.txtHargaProduk.text = data.productVariantPrice.convertToRupiah()
            binding.btnKurang.setOnClickListener {
                data.quantity -= 1
                if (data.quantity > 0) {
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
                        binding.root, binding.root.context.getString(R.string.stok_tidak_mencukupi),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

private class CheckoutDiffCallback :
    DiffUtil.ItemCallback<com.example.ecommerce.core.data.models.request.CheckoutRequest>() {
    override fun areItemsTheSame(
        oldItem: com.example.ecommerce.core.data.models.request.CheckoutRequest,
        newItem: com.example.ecommerce.core.data.models.request.CheckoutRequest
    ): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(
        oldItem: com.example.ecommerce.core.data.models.request.CheckoutRequest,
        newItem: com.example.ecommerce.core.data.models.request.CheckoutRequest
    ): Boolean {
        return oldItem == newItem
    }
}