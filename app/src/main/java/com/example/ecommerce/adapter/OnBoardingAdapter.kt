package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemOnboardingBinding

class OnBoardingAdapter(private val onboardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = onboardingItems[position]
        holder.binding.imageviewOnboarding.setImageResource(item.imageRes)

    }

    override fun getItemCount(): Int = onboardingItems.size

    inner class OnboardingViewHolder(var binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)
}
