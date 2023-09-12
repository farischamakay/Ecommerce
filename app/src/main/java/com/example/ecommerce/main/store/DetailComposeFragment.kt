package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.ecommerce.R

class DetailComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    ScaffoldWithTopBar()
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ScaffoldWithTopBar() {
    val variants = rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detail Product",

                        style = TextStyle(fontFamily = FontFamily(Font(R.font.poppins)),
                            fontSize = 22.sp),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
            )
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),
            ) {
                Divider()
                Box(modifier = Modifier
                    .height(300.dp)){
                    val pagerState = rememberPagerState()
                    HorizontalPager(
                        state = pagerState,
                        pageCount = 5

                    ) { page ->
                        Image(
                            painter = painterResource(id = R.drawable.img_thumbnail_produk),
                            contentDescription = "Andy Rubin",
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(300.dp)
                        )
                    }
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(20.dp)

                            )
                        }
                    }

                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Rp.20.000.000",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins))
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
                                .clickable(onClick = {})
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = {})
                        )
                    }
                }
                Text(
                    text = stringResource(id = R.string.title_product),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Text(
                        text = "Terjual 10",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier
                        .width(5.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.Default.Star ,
                            contentDescription = stringResource(
                            id = R.string.nodesc),
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .size(15.dp)
                            )
                        Text(
                            modifier = Modifier
                                .padding(5.dp),
                            text = "4.5 (2)")
                    }
                }
                Divider()
                Text(
                    text = "Pilih Varian",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins))),
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
                }
                Divider()
                Text(
                    text = "Deskripsi Produk",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins))),
                    modifier = Modifier
                        .padding(top = 10.dp,start = 10.dp, end = 10.dp)
                )
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = "This is description")
                Divider()
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "Ulasan Pembeli",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f))

                    TextButton(onClick = {},
                        modifier = Modifier
                            .padding(10.dp))
                    {

                        Text(text = "Lihat Semua",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontSize = 16.sp)

                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ){
                    Icon(imageVector = Icons.Default.Star ,
                        contentDescription = stringResource(
                            id = R.string.nodesc),
                            modifier = Modifier
                            .padding(start = 5.dp)
                            .size(15.dp)
                    )
                    Text(text = "4.5", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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


