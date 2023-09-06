package com.example.ecommerce.main.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommerce.databinding.FragmentWhistlistBinding

class WishlistFragment : Fragment() {

    private var _binding: FragmentWhistlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhistlistBinding.inflate(layoutInflater)
        return binding.root
    }


}