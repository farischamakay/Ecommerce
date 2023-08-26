package com.example.ecommerce.main.store

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.SearchAdapter
import com.example.ecommerce.databinding.FragmentSearchBinding
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : DialogFragment() {

    private var _binding : FragmentSearchBinding ?= null
    private val binding get() = _binding!!

    private val viewModel : StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListSearch.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SearchAdapter(emptyList()) { _ ->
            dismiss()
        }
        binding.rvListSearch.adapter = adapter


        binding.searchInputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { viewModel.searchItem(it.toString()) }
            }
        })

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                val query = binding.searchInputText.text.toString()
                val bundle = Bundle().apply {
                    putString("query", query)
                }
                requireActivity().supportFragmentManager.setFragmentResult("dataKey", bundle)
                dismiss()
                true
            } else {
                false
            }
        }


        viewModel.searchResult.observe(viewLifecycleOwner){ results ->
            when(results) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE

                }
                is ResourcesResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val response = results.data
                    if(response != null) {
                        adapter.updateData(response.data)
                    }
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}