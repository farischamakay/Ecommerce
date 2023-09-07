package com.example.ecommerce.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.databinding.ItemWishlistGridBinding
import com.example.ecommerce.databinding.ItemWishlistLinearBinding

//class WishlistAdapter() : RecyclerView.Adapter<ReviewAdapter.WishListViewHolder>() {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ReviewAdapter.WishListViewHolder {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBindViewHolder(holder: ReviewAdapter.WishListViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }
//
//    inner class WishListViewHolder (private val binding : ItemWishlistLinearBinding) :
//        RecyclerView.ViewHolder(binding.root){
//            fun bind(data : Cart) {
//                Glide.with(binding.root).load(data.image).into(binding.imgProductView)
//                binding
//            }
//    }
//}