package com.example.ecommerce.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.LoadingStateAdapter
import com.example.ecommerce.adapter.ProductListAdapter
import com.example.ecommerce.databinding.FragmentStoreBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private var sort: String? = null
    private var lowest: String? = null
    private var category: String? = null
    private var highest: String? = null
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductListAdapter


    private val viewModel: StoreViewModel by activityViewModels()

    private val navHostFragment: NavHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridManager = GridLayoutManager(requireContext(), 1)
        binding.rvProductList.layoutManager = gridManager

        productAdapter = ProductListAdapter { itemId ->
            val bundle = bundleOf("id" to itemId?.productId)
            Log.d("BundleId", itemId?.productId.toString())
            navController.navigate(R.id.action_mainFragment_to_detailProductFragment, bundle)
        }

        binding.rvProductList.adapter = productAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                productAdapter.retry()
            }
        )

        binding.swipeLayout.setOnRefreshListener {
            productAdapter.refresh()
            binding.swipeLayout.isRefreshing = false
        }

        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == productAdapter.itemCount && productAdapter.isGrid) {
                    2
                } else {
                    1
                }
            }
        }

        binding.btnChangeGrid.setOnCheckedChangeListener { _, isChecked ->
            productAdapter.isGrid = isChecked
            if (isChecked) {
                gridManager.spanCount = 2
            } else {
                gridManager.spanCount = 1
            }
        }

        binding.edtSearchText.setOnClickListener {
            val fragmentManager: FragmentManager? = activity?.supportFragmentManager
            val dialogFragment = SearchFragment()

            if (fragmentManager != null) {
                dialogFragment.show(fragmentManager, SearchFragment::class.java.simpleName)
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "dataKey", this
        ) { _, bundle ->
            val data = bundle.getString("query")
            binding.fieldSearchProduct.hint = data
            viewModel.setQuery(search = data,
                viewModel.param.value?.brand, viewModel.param.value?.lowest,
                viewModel.param.value?.highest, viewModel.param.value?.sort)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "filterKey", this
        ) { _, bundle ->
            binding.chipGroup.removeAllViews()

            bundle.getString("sort")?.let {
                createChips(it)
                sort = it
            }

            bundle.getString("category")?.let {
                createChips(it)
                category = it
            }

            bundle.getString("lowest").let {
                (it?.toIntOrNull())?.let { low ->
                    createChips("> $low")
                }
                lowest = it
            }

            bundle.getString("highest").let {
                (it?.toIntOrNull())?.let { high ->
                    createChips("< $high")
                }
                highest = it
            }

            viewModel.setQuery(
                search = viewModel.param.value?.search,
                sort = sort, brand = category, lowest = lowest?.toIntOrNull(),
                highest = highest?.toIntOrNull()
            )
        }

        binding.chipFilter.setOnClickListener {
            val fragmentManager: FragmentManager? = activity?.supportFragmentManager
            val dialogFragment = BottomFilterFragment()

            if (fragmentManager != null) {
                dialogFragment.show(fragmentManager, BottomFilterFragment::class.java.simpleName)
            }
        }
        getData()
    }

    private fun getData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.observe(viewLifecycleOwner) { pagingData ->
                productAdapter.submitData(lifecycle, pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            productAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.refresh is LoadState.Loading
                val isError = loadStates.refresh is LoadState.Error
                val isSuccess = loadStates.refresh is LoadState.NotLoading

                binding.layoutError.root.isVisible = isError

                if (isError) {
                    when (val error = (loadStates.refresh as LoadState.Error).error) {
                        is HttpException -> {
                            if (error.code() == 404) {
                                binding.layoutError.apply {
                                    imgErrorConnection
                                    txtErrorCode.text = getString(R.string.empty_code)
                                    txtMsgError
                                    btnReset
                                }

                                binding.layoutError.btnReset.setOnClickListener {
                                    binding.chipGroup.removeAllViews()
                                    viewModel.setQuery(
                                        search = null,
                                        brand = null, lowest = null, highest = null, sort = null
                                    )
                                    binding.fieldSearchProduct.hint = "Search"
                                }
                            }

                            if (error.code() == 500) {
                                binding.layoutError.apply {
                                    imgErrorConnection
                                    txtErrorCode.text = "500"
                                    txtMsgError.text = getString(R.string.internal_server_error)
                                    btnReset.text = getString(R.string.refresh)
                                }

                                binding.layoutError.btnReset.setOnClickListener {
                                    productAdapter.retry()
                                }
                            }
                        }

                        is IOException -> {
                            binding.layoutError.apply {
                                imgErrorConnection
                                txtErrorCode.text = getString(R.string.connection)
                                txtMsgError.text = getString(R.string.connection_error)
                                btnReset.text = getString(R.string.refresh)
                            }

                            binding.layoutError.btnReset.setOnClickListener {
                                productAdapter.retry()
                            }
                        }
                    }
                }
                binding.chipFilter.isVisible = isSuccess
                binding.btnChangeGrid.isVisible = isSuccess
                binding.rvProductList.isVisible = isSuccess
                binding.shimmerLayoutStore.isVisible = isLoading && !productAdapter.isGrid
                binding.shimmerLayoutGrid.isVisible = isLoading && productAdapter.isGrid
            }
        }
    }

    private fun createChips(name: String?) {
        val chip = Chip(requireContext())
        chip.apply {
            text = name
            isClickable = false
            isCheckable = true
            isSelected = true
            binding.apply {
                if (name != null && name != "")
                    chipGroup.addView(chip as View)
            }
        }
    }


}