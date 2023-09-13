package com.example.ecommerce.main.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.example.ecommerce.R

//class ReviewComposeFragment : Fragment() {
//
//    private val viewModel: StoreViewModel by viewModels()
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setContent {
//                MaterialTheme {
//                    ScaffoldWithTopBar()
//                }
//            }
//        }
//    }
//
//    @Preview(showBackground = true, device = Devices.PIXEL_4)
//    @OptIn(
//        ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
//        ExperimentalFoundationApi::class
//    )
//    @Composable
//    fun ScaffoldWithTopBar(){
//        Scaffold(
//            topBar = TopAppBar(title = { /*TODO*/ })
//        )
//    }
//
//}

