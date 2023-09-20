package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.SearchAdapter
import com.example.ecommerce.databinding.FragmentSearchBinding
import com.example.ecommerce.utils.Helpers.showSoftKeyboard
import com.example.ecommerce.utils.ResourcesResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : DialogFragment() {

    @Inject
    lateinit var firebaseAnalyctic : FirebaseAnalytics
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel.param.value?.apply {
            binding.searchInputText.setText(search)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard(requireContext(), binding.searchInputText)

        binding.rvListSearch.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SearchAdapter(emptyList()) { itemId ->
            val bundle = Bundle().apply {
                putString("query", itemId)
            }
            requireActivity().supportFragmentManager.setFragmentResult("dataKey", bundle)
            dismiss()
        }

        binding.rvListSearch.adapter = adapter

        binding.searchInputText.doAfterTextChanged { e ->
            e?.let { viewModel.searchItem(it.toString()) }
        }

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
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


        viewModel.searchResult.observe(viewLifecycleOwner) { results ->
            when (results) {
                is ResourcesResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE

                }

                is ResourcesResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }

                is ResourcesResult.Success -> {
                    val search = binding.searchInputText.text.toString()
                    firebaseAnalyctic.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS){
                        param(FirebaseAnalytics.Param.SEARCH_TERM, search)
                    }
                    binding.progressBar.visibility = View.GONE
                    val response = results.data
                    if (response != null) {
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