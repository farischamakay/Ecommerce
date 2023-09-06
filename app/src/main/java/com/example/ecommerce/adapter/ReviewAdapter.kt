package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.models.response.ReviewDataItem
import com.example.ecommerce.databinding.ItemUlasanPembeliBinding

class ReviewAdapter(private var reviews: List<ReviewDataItem>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapterViewHolder {
        val binding = ItemUlasanPembeliBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ReviewAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewAdapterViewHolder, position: Int) {
        val items = reviews[position]
        Glide.with(holder.itemView.context).load(items.userImage)
            .into(holder.binding.imgProfileUser)
        holder.binding.txtUsernameUser.text = items.userName
        if (items.userRating != null) {
            holder.binding.rBar.rating = items.userRating.toFloat()
        }
        holder.binding.txtReviewUser.text = items.userReview
    }

    inner class ReviewAdapterViewHolder(var binding: ItemUlasanPembeliBinding) :
        RecyclerView.ViewHolder(binding.root)

}