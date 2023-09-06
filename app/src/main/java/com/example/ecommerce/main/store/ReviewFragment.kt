package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.ReviewAdapter
import com.example.ecommerce.databinding.FragmentReviewBinding
import com.example.ecommerce.utils.ResourcesResult

class ReviewFragment : Fragment() {

    private val viewModel: StoreViewModel by activityViewModels()

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id: ReviewFragmentArgs by navArgs()
        viewModel.reviewItem(id.toString())

        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.reviewProduct.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResourcesResult.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val response = result.data?.data
                    if (response != null) {
                        val nonNullItems = response.filterNotNull()
                        val adapter = ReviewAdapter(nonNullItems)
                        binding.rvReview.adapter = adapter
                    }
                }

                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()

                }
            }
        }

    }

}