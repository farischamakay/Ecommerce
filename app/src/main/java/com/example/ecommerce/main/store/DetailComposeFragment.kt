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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.data.models.request.ListCheckout
import com.example.ecommerce.data.models.response.ProductDetailData
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.utils.ComposeTheme
import com.example.ecommerce.utils.ErrorStateCompose
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.convertToRupiah
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import convertToCart
import convertToCheckout
import convertToWishlist
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailComposeFragment : Fragment() {

    private val viewModel: StoreViewModel by viewModels()
    private lateinit var dataObserve: ProductDetailData
    lateinit var productId: String
    lateinit var params : Bundle
    @Inject lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ComposeTheme {
                    ScaffoldWithTopBar()
                }
            }
        }
    }


    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    fun ScaffoldWithTopBar(getProduct: ResourcesResult<ProductDetailResponse?>) {
        val getDataRoom by viewModel.getDataRoom.observeAsState()
        val getWishlist by viewModel.getDataWishlist.observeAsState()
        var currentIndex by rememberSaveable { mutableStateOf(0) }
        var priceSum: Int? = null
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.detail_product),
                            style = TextStyle(
                                fontSize = 22.sp
                            ),
                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            findNavController().navigateUp()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    },
                )
            },
            content = { innerPadding ->
                when (getProduct) {

                    is ResourcesResult.Loading -> {
                        Loading()
                    }

                    is ResourcesResult.Success -> {

                        dataObserve = getProduct
                            .data?.data!!
                        val isProductInWishList = getWishlist?.any { it.productId == productId }


                        params = Bundle()
                        params.putString(FirebaseAnalytics.Param.CURRENCY, "IDR")
                        params.putDouble(FirebaseAnalytics.Param.PRICE, dataObserve.productPrice?.toDouble() ?: 0.00)
                        params.putString(FirebaseAnalytics.Param.ITEM_NAME, dataObserve.productName ?: "")
                        params.putString(FirebaseAnalytics.Param.ITEM_BRAND, dataObserve.brand ?: "")

                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)

                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
//                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Divider()
                            Box(
                                modifier = Modifier
                                    .height(300.dp)
                            ) {
                                val pagerState = rememberPagerState()
                                dataObserve.image?.size?.let {
                                    HorizontalPager(
                                        state = pagerState,
                                        pageCount = it
                                    ) { page ->
                                        AsyncImage(
                                            model = dataObserve.image!![page],
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .size(300.dp)
                                        )
                                    }
                                }
                                if ((dataObserve.image?.size ?: 0) > 1) {
                                    Row(
                                        Modifier
                                            .height(50.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        dataObserve.image?.size?.let {
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
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                    .fillMaxWidth(),
                            ) {
                                if (dataObserve.productPrice != null) {
                                    val price =
                                        dataObserve.productVariant[currentIndex].variantPrice
                                    priceSum = dataObserve.productPrice?.plus(price)
                                    Text(
                                        text = priceSum!!.convertToRupiah(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )
                                }
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
                                                val share: Intent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(
                                                        Intent.EXTRA_TEXT,
                                                        "${dataObserve.productName}\n" +
                                                                "${dataObserve.productPrice}\n" +
                                                                "http://ecommerce.farischa.com/product/${dataObserve.productId}"
                                                    )
                                                    type = "text/plain"
                                                }

                                                val shareIntent = Intent.createChooser(share, null)
                                                startActivity(shareIntent)
                                            })
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        imageVector = if (isProductInWishList == true) {
                                            Icons.Default.Favorite
                                        } else {
                                            Icons.Default.FavoriteBorder
                                        },
                                        contentDescription = "Favorite",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable(onClick = {
                                                if (isProductInWishList == true) {
                                                    viewModel.deleteItemById(productId)
                                                    view?.let {
                                                        Snackbar
                                                            .make(
                                                                it,
                                                                getString(R.string.product_sudah_ada_di_wishlist),
                                                                Snackbar.LENGTH_LONG
                                                            )
                                                            .show()
                                                    }
                                                } else {
                                                    convertToWishlist(
                                                        dataObserve
                                                    )
                                                        .let { viewModel.insertToWishlist(it) }
                                                    view?.let {
                                                        Snackbar
                                                            .make(
                                                                it,
                                                                getString(R.string.product_ditambahkan_pada_wishlist),
                                                                Snackbar.LENGTH_LONG
                                                            )
                                                            .show()
                                                    }
                                                }
                                            })
                                    )
                                }
                            }
                            dataObserve.productName?.let {
                                Text(
                                    text = it,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp),
                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Terjual ${dataObserve.sale}",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
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
                                        text = "${dataObserve.productRating} (2)",
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )
                                }
                            }
                            Divider()
                            Text(
                                text = stringResource(R.string.pilih_varian),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 16.dp)
                            )
                            FlowRow(
                                Modifier
                                    .fillMaxWidth(1f)
                                    .wrapContentHeight(align = Alignment.Top)
                                    .padding(start = 16.dp),
                                horizontalArrangement = Arrangement.Start
                            )
                            {
                                dataObserve.productVariant.forEachIndexed { index, productVariantItem ->
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
                                text = stringResource(R.string.deskripsi_produk),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,

                                    ),
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 16.dp, end = 10.dp)
                            )
                            dataObserve.description?.let {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 16.dp),
                                    text = it,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                                )
                            }
                            Divider()
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                Text(
                                    text = stringResource(R.string.ulasan_pembeli),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                TextButton(onClick = {
                                    findNavController().navigate(
                                        DetailComposeFragmentDirections
                                            .actionDetailProductFragmentToReviewFragment(productId)
                                    )
                                })
                                {

                                    Text(
                                        text = getString(R.string.lihat_semua),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )

                                }
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, bottom = 20.dp)
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
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = dataObserve.productRating.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )
                                    Text(
                                        text = "/5.0",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
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
                                        text = "${dataObserve.totalSatisfaction}% pembeli merasa puas",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )
                                    Text(
                                        text = "${dataObserve.totalRating} Rating . " +
                                                "${dataObserve.totalReview} Ulasan",
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )

                                }
                            }
                        }
                    }

                    is ResourcesResult.Failure -> {
                        ErrorStateCompose(errorCode = "Empty",
                            errorInfo = "Your requested data is unavailable",
                            resetClick = {
                                viewModel.detailItem(productId)

                            })
                    }
                }

            },
            bottomBar = {
                if (getProduct is ResourcesResult.Success) {
                    Divider()
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                findNavController().navigate(
                                    DetailComposeFragmentDirections
                                        .actionDetailProductFragmentToCheckoutFragment(
                                            ListCheckout(
                                                listCheckout = mutableListOf(
                                                    convertToCheckout(dataObserve, currentIndex)
                                                )
                                            ), "", ""
                                        )
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = getString(R.string.beli_langsung),
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        }

                        Button(
                            onClick = {
                                val cartData = getDataRoom?.find { it.productId == productId }

                                val itemProductCart = Bundle(params).apply{
                                    putInt(FirebaseAnalytics.Param.QUANTITY, cartData?.quantity?: 0)
                                }

//                                firebaseAnalytics.logEvent()

                                if (cartData == null) {
                                    val dataNew = dataObserve.copy(productPrice = priceSum)
                                    viewModel.insertToRoom(convertToCart(dataNew, currentIndex))
                                    view?.let {
                                        Snackbar
                                            .make(
                                                it,
                                                getString(R.string.product_ditambahkan_pada_keranjang),
                                                Snackbar.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                } else {
                                    var qtyCart = cartData.quantity
                                    if (qtyCart < (dataObserve.stock ?: 0)) {
                                        qtyCart += 1
                                        viewModel.updateQuantity(
                                            listOf(
                                                convertToCart(dataObserve, currentIndex) to qtyCart
                                            )
                                        )
                                        view?.let {
                                            Snackbar.make(
                                                it,
                                                getString(R.string.product_berhasil_ditambahkan_pada_keranjang),
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        view?.let {
                                            Snackbar.make(
                                                it, getString(R.string.stok_tidak_mencukupi),
                                                Snackbar.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text(
                                text = getString(R.string.keranjang),
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun ScaffoldWithTopBar() {
        productId = arguments?.getString("id").toString()
        viewModel.detailItem(productId)
        val getProduct by viewModel.detailProduct.observeAsState(initial = ResourcesResult.Loading)
        ScaffoldWithTopBar(getProduct)
    }

    @Composable
    fun Loading() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Magenta
            )
        }
    }
}


