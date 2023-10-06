package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.core.data.models.response.ReviewDataItem
import com.example.ecommerce.databinding.ItemUlasanPembeliBinding

class ReviewAdapter :
    ListAdapter<ReviewDataItem, ReviewAdapter.ReviewAdapterViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapterViewHolder {
        val binding = ItemUlasanPembeliBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ReviewAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapterViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)

    }

    inner class ReviewAdapterViewHolder(var binding: ItemUlasanPembeliBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReviewDataItem) {
            Glide.with(binding.root).load(data.userImage).circleCrop()
                .error(R.drawable.img_thumbnail_produk)
                .into(binding.imgProfileUser)
            binding.txtUsernameUser.text = data.userName
            if (data.userRating != null) {
                binding.rBar.rating = data.userRating!!.toFloat()
            }
            binding.txtReviewUser.text = data.userReview
        }
    }
}

private class ReviewDiffCallback : DiffUtil.ItemCallback<ReviewDataItem>() {
    override fun areItemsTheSame(oldItem: ReviewDataItem, newItem: ReviewDataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ReviewDataItem, newItem: ReviewDataItem): Boolean {
        return oldItem == newItem
    }

}