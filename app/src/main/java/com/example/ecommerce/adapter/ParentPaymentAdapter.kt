package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.data.models.response.PaymentType
import com.example.ecommerce.databinding.ItemListParentPaymentBinding

class ParentPaymentAdapter : ListAdapter<PaymentType, ParentPaymentAdapter.ParentPaymentViewHolder>
    (ParentPaymentDiffUtil()) {

    val adapter = PaymentChildAdapter()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentPaymentAdapter.ParentPaymentViewHolder {
        val binding = ItemListParentPaymentBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ParentPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ParentPaymentAdapter.ParentPaymentViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.binding.txtTitleType.text = item.title
        holder.binding.rvPayment.layoutManager = LinearLayoutManager(holder.binding.root.context)
        holder.binding.rvPayment.adapter = adapter
        adapter.submitList(item.item)


    }
    inner class ParentPaymentViewHolder (val binding : ItemListParentPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private class ParentPaymentDiffUtil : DiffUtil.ItemCallback<PaymentType>() {
    override fun areItemsTheSame(oldItem: PaymentType, newItem: PaymentType): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: PaymentType, newItem: PaymentType): Boolean {
        return oldItem == newItem
    }
}