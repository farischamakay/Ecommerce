package com.example.ecommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.databinding.FragmentMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalBadgeUtils @AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MainViewModel by viewModels()
    private val navHostFragment: NavHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.nhf_botnav) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nhfBotnav
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener {
        }

        viewModel.getDataRoom.observe(viewLifecycleOwner){response ->

                val cartBadges = BadgeDrawable.create(requireContext())
                cartBadges.number = response.size

                attachBadgeDrawable(cartBadges,binding.topAppBar, R.id.cart)

        }


        val wishListBadges = binding.bottomNavigation.getOrCreateBadge(R.id.wishlistFragment)
        wishListBadges.number = viewModel.getDataWishlist.value?.size ?: 0

        binding.topAppBar.title = viewModel.getUsername()
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> {
                    (requireActivity() as MainActivity).goToCart()
                    true
                }

                R.id.notification -> {
                    navController.navigate(R.id.main_to_prelogin)
                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}