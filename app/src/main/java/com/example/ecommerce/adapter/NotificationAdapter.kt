package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.databinding.ItemListNotifikasiBinding
import com.google.android.material.color.MaterialColors

class NotificationAdapter : ListAdapter<Notification,
        NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    interface OnItemClickCallback {
        fun onItemClicked(notification: Notification)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val binding = ItemListNotifikasiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        val items = getItem(position)
        holder.bind(items)
    }

    inner class NotificationViewHolder(val binding: ItemListNotifikasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notification) {
            val backgroundColor = MaterialColors.getColor(
                binding.root.rootView,
                com.google.android.material.R.attr.colorPrimaryContainer
            )
            binding.root.setOnClickListener {
                if (!data.isSelected) {
                    data.isSelected = true
                    onItemClickCallback.onItemClicked(data)
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor
                            (binding.root.context, R.color.white)
                    )
                }
            }
            Glide.with(binding.root).load(data.image).into(binding.imgNotification)
            binding.apply {
                txtInfoNotif.text = data.type
                txtDateNotification.text = data.date
                txtTimeOfNotification.text = data.time
                txtStatusInfo.text = data.title
                txtDescNotif.text = data.body

                if (!data.isSelected) {
                    root.setBackgroundColor(backgroundColor)
                }
            }
        }
    }
}

private class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

}