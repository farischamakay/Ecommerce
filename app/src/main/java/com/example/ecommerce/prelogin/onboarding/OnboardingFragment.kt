package com.example.ecommerce.prelogin.onboarding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerce.R
import com.example.ecommerce.adapter.OnBoardingAdapter
import com.example.ecommerce.adapter.OnBoardingItem
import com.example.ecommerce.databinding.FragmentOnboardingBinding
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var indicatorContainer: LinearLayout
    private val viewModel: OnboardingViewModel by viewModels()
    private val onboardingItems = listOf(
        OnBoardingItem(R.drawable.img_onboarding_one),
        OnBoardingItem(R.drawable.img_onboarding_two),
        OnBoardingItem(R.drawable.img_onboarding_three)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isOnboardingCompleted()) {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIndicators()
        setCurrentIndicator(0)

        val adapter = OnBoardingAdapter(onboardingItems)
        binding.viewpagerOnboarding.adapter = adapter


        binding.viewpagerOnboarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)

                if (position == adapter.itemCount - 1) {
                    binding.btnSelanjutnya.visibility = View.INVISIBLE
                } else {
                    binding.btnSelanjutnya.visibility = View.VISIBLE
                }
            }
        })
        (binding.viewpagerOnboarding.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        binding.btnToRegister.setOnClickListener {
            viewModel.markOnboardingCompleted()
            findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
        }


        binding.btnLewati.setOnClickListener {
            viewModel.markOnboardingCompleted()
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)

        }

        binding.btnSelanjutnya.setOnClickListener {
            val nextSlide = binding.viewpagerOnboarding.currentItem + 1
            if (nextSlide < adapter.itemCount) {
                binding.viewpagerOnboarding.setCurrentItem(nextSlide, true)
            }
        }
    }

    private fun setupIndicators() {
        indicatorContainer = binding.indicatorsContainer
        val indicator = arrayOfNulls<ImageView>(onboardingItems.size)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(requireContext())
            indicator[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_indicator_inactive
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
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