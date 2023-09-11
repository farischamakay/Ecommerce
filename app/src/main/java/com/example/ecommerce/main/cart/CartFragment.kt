package com.example.ecommerce.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapter.CartAdapter
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.models.request.toListCheckout
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.utils.convertToRupiah
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()

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
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.checkboxParent.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selectedAllItems(isChecked)
        }

        cartAdapter = CartAdapter()
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.itemAnimator?.changeDuration = 0

        cartAdapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
            override fun onItemClicked(cart: List<Pair<Cart, Boolean>>) {
                viewModel.updateCheckable(cart)
            }

            override fun counterClicked(cart: List<Pair<Cart, Int>>) {
                viewModel.updateQuantity(cart)
            }

            override fun onDeleteClicked(itemId: String) {
                viewModel.deleteItemById(itemId)
            }
        })


        viewModel.getDataRoom.observe(viewLifecycleOwner) { response ->
            cartAdapter.submitList(response)

            binding.emptyState.root.isVisible = response.isNullOrEmpty()

            val isSelected = response.filter { it.isCheck }
            val checkListCheckBox = response.any { it.isCheck }
            var totalPrice = 0
            val allChecked = response.all { it.isCheck }

            binding.checkboxParent.isChecked = allChecked


            response.map {
                if (it.isCheck) {
                    totalPrice += it.productVariantPrice * it.quantity
                }
            }

            if (checkListCheckBox) {
                binding.btnBayar.isEnabled = true
                binding.btnDeleteList.visibility = View.VISIBLE
                binding.txtTotalBayar.text = totalPrice.convertToRupiah()
                binding.btnDeleteList.setOnClickListener {
                    viewModel.deleteCheckedItems()
                }
                binding.btnBayar.setOnClickListener {
                    navController.navigate(CartFragmentDirections.
                    actionCartFragmentToCheckoutFragment(isSelected.toListCheckout(), "" , ""))
                }
            } else {
                binding.btnBayar.isEnabled = false
                binding.txtTotalBayar.text = getString(R.string.rp_0)
                binding.btnDeleteList.visibility = View.GONE
            }

        }
    }
}