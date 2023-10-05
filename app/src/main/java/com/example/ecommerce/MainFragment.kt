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
import com.example.ecommerce.utils.Constants
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalBadgeUtils
@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
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

        binding.apply {
            nhfBotnav
            bottomNavigation?.setupWithNavController(navController)
            bottomNavigation?.setOnItemReselectedListener { }
            botnav600?.setupWithNavController(navController)
            botnav600?.setOnItemReselectedListener { }
            botnav840?.setupWithNavController(navController)
            botnav840?.setOnItemReselectedListener { }
        }

        val cartBadges = BadgeDrawable.create(requireContext())
        viewModel.getDataRoom.observe(viewLifecycleOwner) { response ->
            cartBadges.isVisible = response.isNotEmpty()
            cartBadges.number = response.size
        }
        attachBadgeDrawable(cartBadges, binding.topAppBar, R.id.cart)

        viewModel.getDataWishlist.observe(viewLifecycleOwner) { response ->
            val wishListBadges = binding.bottomNavigation?.getOrCreateBadge(R.id.wishlistFragment)
            wishListBadges?.isVisible = response.isNotEmpty()
            wishListBadges?.number = response.size
        }

        viewModel.getDataWishlist.observe(viewLifecycleOwner) { response ->
            val wishListBadges = binding.botnav600?.getOrCreateBadge(R.id.wishlistFragment)
            wishListBadges?.isVisible = response.isNotEmpty()
            wishListBadges?.number = response.size
        }

        viewModel.getDataWishlist.observe(viewLifecycleOwner) { response ->
            val wishListBadges = binding.botnav840?.getOrCreateBadge(R.id.wishlistFragment)
            wishListBadges?.isVisible = response.isNotEmpty()
            wishListBadges?.number = response.size
        }


        val notifBadges = BadgeDrawable.create(requireContext())
        viewModel.getDataNotification.observe(viewLifecycleOwner) { response ->
            val new = response.filter { !it.isSelected }
            notifBadges.isVisible = response.isNotEmpty()
            notifBadges.number = new.size
        }
        attachBadgeDrawable(notifBadges, binding.topAppBar, R.id.notification)

        binding.topAppBar.title = viewModel.getUsername()
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> {
                    firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                        param(Constants.BUTTON_NAME, "cart")
                    }
                    (requireActivity() as MainActivity).goToCart()
                    true
                }

                R.id.notification -> {
                    firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                        param(Constants.BUTTON_NAME, "notification")
                    }
                    (requireActivity() as MainActivity).goToNotification()
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