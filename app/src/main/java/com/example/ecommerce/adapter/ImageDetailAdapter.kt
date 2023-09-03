package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ItemOnboardingBinding

class ImageDetailAdapter(private val imageItems: List<String?>) :
    RecyclerView.Adapter<ImageDetailAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = imageItems[position]
        Glide.with(holder.binding.root).load(item).into(holder.binding.imageviewOnboarding)
    }

    override fun getItemCount(): Int = imageItems.size

    inner class OnboardingViewHolder(var binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)
}