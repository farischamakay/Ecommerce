package com.example.ecommerce.prelogin.onboarding

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.putString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerce.R
import com.example.ecommerce.adapter.OnBoardingAdapter
import com.example.ecommerce.data.model.OnBoardingItem
import com.example.ecommerce.databinding.FragmentOnboardingBinding
import com.google.android.material.R.color.m3_ref_palette_primary40

class OnboardingFragment : Fragment() {

    private lateinit var indicatorContainer : LinearLayout

    private val onboardingItems = listOf(
        OnBoardingItem(R.drawable.img_onboarding_one),
        OnBoardingItem(R.drawable.img_onboarding_two),
        OnBoardingItem(R.drawable.img_onboarding_three)
    )

    private var _binding : FragmentOnboardingBinding ?= null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIndicators()
        setCurrentIndicator(0)

        val adapter = OnBoardingAdapter(onboardingItems)
        binding.viewpagerOnboarding.adapter = adapter


        binding.viewpagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)

                if (position == adapter.itemCount - 1) {
                    binding.btnLewati.visibility = View.INVISIBLE
                } else {
                    binding.btnLewati.visibility = View.VISIBLE
                }
            }
        })
        (binding.viewpagerOnboarding.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.btnToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.btnLewati.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)

        }

        binding.btnSelanjutnya.setOnClickListener {
            val nextSlide = binding.viewpagerOnboarding.currentItem + 1
            if(nextSlide < adapter.itemCount) {
                binding.viewpagerOnboarding.setCurrentItem(nextSlide, true)
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            }
        }
    }

    private fun setupIndicators(){
        indicatorContainer = binding.indicatorsContainer
        val indicator = arrayOfNulls<ImageView>(onboardingItems.size)
        val layoutParams : LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicator.indices){
            indicator[i] = ImageView(requireContext())
            indicator[i]?.let {
                it.setImageDrawable(ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.shape_indicator_inactive)
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position : Int) {
        val childCount = indicatorContainer.childCount
        for(i in 0 until childCount){
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if(i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_indicator_inactive
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}