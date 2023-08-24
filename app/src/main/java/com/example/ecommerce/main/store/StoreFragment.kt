package com.example.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.LoadingStateAdapter
import com.example.ecommerce.adapter.ProductListAdapter
import com.example.ecommerce.data.models.request.ProductRequest
import com.example.ecommerce.databinding.FragmentStoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private var _binding : FragmentStoreBinding ?= null
    private val binding get() = _binding!!
    private val viewModel : StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())

        binding.topAppBar.title = viewModel.getUsername()
        binding.chipFilter.setOnClickListener {
            showBottomSheetDialog()
        }

        getData()

    }

    private fun getData(){
        val adapter = ProductListAdapter()
        binding.rvProductList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.product(ProductRequest()).observe(viewLifecycleOwner){
            adapter.submitData(lifecycle,it)
            Log.d("FragmentRvData", it.toString())
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}