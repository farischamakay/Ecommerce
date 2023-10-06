package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.core.data.models.response.PaymentItem
import com.example.ecommerce.databinding.ItemListPaymentBinding


class PaymentChildAdapter : ListAdapter<PaymentItem, PaymentChildAdapter.PaymentChildViewHolder>
    (ChildDiffUtil()) {

    private var onItemClickListener: ((PaymentItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (PaymentItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentChildAdapter.PaymentChildViewHolder {
        val binding =
            ItemListPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentChildViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PaymentChildAdapter.PaymentChildViewHolder,
        position: Int
    ) {
        val items = getItem(position)
        holder.bind(items)
    }

    inner class PaymentChildViewHolder(val binding: ItemListPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PaymentItem) {
            Glide.with(binding.root).load(data.image).into(binding.imgCardView)
            binding.txtChosePayment.text = data.label

            if (data.status == false) {
                binding.root.alpha = 0.5f
            } else {
                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(data)
                }
            }
        }
    }
}

private class ChildDiffUtil : DiffUtil.ItemCallback<PaymentItem>() {
    override fun areItemsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
        return oldItem.label == newItem.label
    }

    override fun areContentsTheSame(oldItem: PaymentItem, newItem: PaymentItem): Boolean {
        return oldItem == newItem
    }
}