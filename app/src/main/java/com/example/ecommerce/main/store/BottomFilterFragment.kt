package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.example.ecommerce.databinding.FragmentBottomFilterBinding
import com.example.ecommerce.utils.Helpers.setSelectedChip
import com.example.ecommerce.utils.emptyToNull
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip


class BottomFilterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by activityViewModels()
    private var sort = View.NO_ID
    private var sortChip: Chip? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contextThemeWrapper = ContextThemeWrapper(
            requireContext(),
            com.example.ecommerce.R.style.Base_Theme_Ecommerce
        )
        _binding = FragmentBottomFilterBinding.inflate(
            inflater.cloneInContext((contextThemeWrapper)),
            container, false
        )

        viewModel.param.value?.apply {
            sort?.let { binding.chipGroupUrutkan.setSelectedChip(it) }
            brand?.let { binding.chipGroupKategori.setSelectedChip(it) }
            lowest?.let { binding.edtHargaTerendah.setText(it.toString()) }
            highest?.let { binding.edtHargaTertinggi.setText(it.toString()) }
        }

        viewModel.param.observe(viewLifecycleOwner) { response ->
            if (!response.sort.isNullOrEmpty()) {
                binding.btnResetFilter.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val behavior = BottomSheetBehavior.from(binding.root.parent as View)
        val chipGroupSort = binding.chipGroupUrutkan
        val chipGroupCategory = binding.chipGroupKategori
        val edtHighest = binding.edtHargaTertinggi
        val edtLowest = binding.edtHargaTerendah

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        updateResetButtonVisibility()

        chipGroupSort.setOnCheckedStateChangeListener { _, _ ->
            viewModel.isSortChipGroupChecked = chipGroupSort.checkedChipId != View.NO_ID
            updateResetButtonVisibility()
        }

        chipGroupCategory.setOnCheckedStateChangeListener { _, _ ->
            viewModel.isCategoryChipGroupChecked = chipGroupCategory.checkedChipId != View.NO_ID
            updateResetButtonVisibility()
        }

        edtHighest.doOnTextChanged { _, _, _, _ ->
            viewModel.isHighestTextView = !edtHighest.text.isNullOrEmpty()
            updateResetButtonVisibility()
        }

        edtLowest.doOnTextChanged { _, _, _, _ ->
            viewModel.isLowestTextView = !edtLowest.text.isNullOrEmpty()
            updateResetButtonVisibility()
        }

        binding.btnResetFilter.setOnClickListener {
            edtHighest.text?.clear()
            edtLowest.text?.clear()
            chipGroupSort.clearCheck()
            chipGroupCategory.clearCheck()
        }

        binding.btnTampilkanProduk.setOnClickListener {
            sort = binding.chipGroupUrutkan.checkedChipId
            sortChip = binding.chipGroupUrutkan.findViewById(sort)
            val sortOk = sortChip?.text ?: ""

            val category = binding.chipGroupKategori.checkedChipId
            val categoryChip = binding.chipGroupKategori.findViewById<Chip>(category)
            val categoryOk = categoryChip?.text?.toString()

            val lowest = binding.edtHargaTerendah.text.toString()
            val highest = binding.edtHargaTertinggi.text.toString()

            viewModel.setQuery(
                search = viewModel.param.value?.search,
                sort = sortOk.toString().emptyToNull(),
                brand = categoryOk.emptyToNull(),
                lowest = lowest.toIntOrNull(),
                highest = highest.toIntOrNull()
            )

            val bundle = Bundle().apply {
                putString("sort", sortOk.toString())
                putString("category", categoryOk)
                putString("lowest", lowest)
                putString("highest", highest)
            }

            requireActivity().supportFragmentManager.setFragmentResult("filterKey", bundle)

            dismiss()
        }
    }

    private fun updateResetButtonVisibility() {
        binding.btnResetFilter.isVisible =
            viewModel.isSortChipGroupChecked || viewModel.isCategoryChipGroupChecked
                    || viewModel.isHighestTextView || viewModel.isLowestTextView
    }

}