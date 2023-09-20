package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.ecommerce.databinding.FragmentBottomFilterBinding
import com.example.ecommerce.utils.Helpers.setSelectedChip
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomFilterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StoreViewModel by activityViewModels()

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val behavior = BottomSheetBehavior.from(binding.root.parent as View)

        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        binding.btnResetFilter.setOnClickListener {
            binding.edtHargaTertinggi.text?.clear()
            binding.edtHargaTerendah.text?.clear()
            binding.chipGroupUrutkan.clearCheck()
            binding.chipGroupKategori.clearCheck()
            binding.btnResetFilter.isVisible = false
        }

        binding.btnTampilkanProduk.setOnClickListener {

            val sort = binding.chipGroupUrutkan.checkedChipId
            val sortChip = binding.chipGroupUrutkan.findViewById<Chip>(sort)
            val sortOk = sortChip?.text?.toString()

            val category = binding.chipGroupKategori.checkedChipId
            val categoryChip = binding.chipGroupKategori.findViewById<Chip>(category)
            val categoryOk = categoryChip?.text?.toString()

            val lowest = binding.edtHargaTerendah.text.toString()
            val highest = binding.edtHargaTertinggi.text.toString()


            viewModel.setQuery(
                search = viewModel.param.value?.search,
                sort = sortOk, brand = categoryOk, lowest = lowest.toIntOrNull(),
                highest = highest.toIntOrNull()
            )

//            requireActivity().supportFragmentManager.setFragmentResult("filterKey", bundle)
            dismiss()
        }

    }


}