package com.example.ecommerce.main.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar
import convertToWishlist
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailComposeFragment : Fragment() {

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    ScaffoldWithTopBar()
                }
            }
        }
    }


    @Preview(showBackground = true, device = Devices.PIXEL_4)
    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    fun ScaffoldWithTopBar() {
        val getProduct by viewModel.detailProduct.observeAsState(initial = ResourcesResult.Loading)
        val getDataRoom by viewModel.getDataRoom.observeAsState()
        val getWishlist by viewModel.getDataWishlist.observeAsState()
        val productId = arguments?.getString("id").toString()
        viewModel.detailItem(productId)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Detail Product",
                            style = TextStyle(
                                fontSize = 22.sp
                            ),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            findNavController().navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                    ),
                )
            },
            content = { innerPadding ->
                when(getProduct){

                    is ResourcesResult.Loading -> {
                        Loading()
                    }

                    is ResourcesResult.Success -> {
                        val data = (getProduct as ResourcesResult.Success<ProductDetailResponse?>)
                            .data?.data
                        val isProductInWishList = getWishlist?.any { it.productId == productId }


                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(Color.White)
//                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Divider()
                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                            ) {
                                val pagerState = rememberPagerState()
                                data?.image?.size?.let {
                                    HorizontalPager(
                                        state = pagerState,
                                        pageCount = it
                                    ) { page ->
                                        AsyncImage(
                                            model = data.image[page],
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .size(300.dp)
                                        )
                                    }
                                }
                                Row(
                                    Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    data?.image?.size?.let {
                                        repeat(it) { iteration ->
                                            val color =
                                                if (pagerState.currentPage == iteration) Color
                                                    .DarkGray else Color.LightGray
                                            Box(
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .clip(CircleShape)
                                                    .background(color)
                                                    .size(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = "${data?.productPrice?.convertToRupiah()}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End

                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "share",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable(onClick = {
                                                val share : Intent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, "${data?.productName}\n" +
                                                "${data?.productPrice}\n" +
                                                "http://ecommerce.farischa.com/product/${data?.productId}")
                                                type = "text/plain"
                                            }

                                            val shareIntent = Intent.createChooser(share, null)
                                            startActivity(shareIntent)
                                            })
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        imageVector = if(isProductInWishList == true){
                                            Icons.Default.Favorite }
                                        else {
                                            Icons.Default.FavoriteBorder
                                                                          },
                                        contentDescription = "Favorite",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable(onClick = {
                                                if (isProductInWishList == true) {
                                                    viewModel.deleteItemById(productId)
                                                    view?.let {
                                                        Snackbar.make(
                                                            it, "Product sudah ada di Wishlist!",
                                                            Snackbar.LENGTH_LONG).show()
                                                    }
                                                } else {
                                                    data?.let {
                                                        convertToWishlist(
                                                            it
                                                        )
                                                    }?.let { viewModel.insertToWishlist(it) }
                                                    view?.let {
                                                        Snackbar.make(
                                                            it, "Product ditambahkan pada Wishlist!",
                                                            Snackbar.LENGTH_LONG).show()
                                                    }
                                                }
                                            })
                                    )
                                }
                            }
                            data?.productName?.let {
                                Text(
                                    text = it,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Terjual ${data?.sale}",
                                    fontSize = 12.sp,
                                    
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(5.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = stringResource(
                                            id = R.string.nodesc
                                        ),
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                            .size(15.dp)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(5.dp),
                                        text = "${data?.productRating} (2)"
                                    )
                                }
                            }
                            Divider()
                            Text(
                                text = "Pilih Varian",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    
                                ),
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                            FlowRow(
                                Modifier
                                    .fillMaxWidth(1f)
                                    .wrapContentHeight(align = Alignment.Top)
                                    .padding(horizontal = 15.dp),
                                horizontalArrangement = Arrangement.Start
                            )
                            {
                                var currentIndex by remember{ mutableStateOf(0) }
                                data?.productVariant?.forEachIndexed { index, productVariantItem ->
                                    val isSelected = index == currentIndex
                                    InputChip(
                                        modifier = Modifier
                                            .padding(5.dp),
                                        onClick = {
                                            currentIndex = index
                                        },
                                        label = { Text(productVariantItem.variantName) },
                                        selected = isSelected,
                                    )
                                }
                            }
                            Divider()
                            Text(
                                text = "Deskripsi Produk",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    
                                ),
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            )
                            data?.description?.let {
                                Text(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    text = it
                                )
                            }
                            Divider()
                            Row(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    text = "Ulasan Pembeli",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                TextButton(onClick = {})
                                {

                                    Text(
                                        text = "Lihat Semua",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )

                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = stringResource(
                                            id = R.string.nodesc
                                        ),
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                            .size(15.dp)
                                    )
                                    Text(
                                        text = data?.productRating.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        
                                    )
                                    Text(
                                        text = "/5.0",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .width(20.dp)
                                )
                                Column(
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = "${data?.totalSatisfaction}% pembeli merasa puas",
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "${data?.totalRating} Rating . ${data?.totalReview} Ulasan",
                                        
                                    )

                                }
                            }
                        }
                    }
                    
                    is ResourcesResult.Failure -> {

                    }
                }

            },
            bottomBar = {
                Divider()
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text("Beli Langsung")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(text = "+ Keranjang")
                    }
                }
            }
        )
    }

    @Composable
    fun Loading(){
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center){
            CircularProgressIndicator(
                color = Color.Magenta
            )
        }
    }
}


