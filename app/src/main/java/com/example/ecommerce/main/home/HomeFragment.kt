package com.example.ecommerce.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title = viewModel.getUserNameLogin()

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> {
                    navController.navigate(R.id.action_mainFragment_to_cartFragment)
                    true
                }

                R.id.notification -> {
                    navController.navigate(R.id.main_to_prelogin)
                    true
                }

                else -> false
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.deleteToken()
            (requireActivity() as MainActivity).logOut()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding != null
    }

}