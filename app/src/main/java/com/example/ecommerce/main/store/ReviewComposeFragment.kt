package com.example.ecommerce.main.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.core.data.models.response.ReviewResponse
import com.example.ecommerce.utils.ComposeTheme
import com.example.ecommerce.utils.ErrorStateCompose
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReviewComposeFragment : Fragment() {

    private val viewModel: StoreViewModel by viewModels()
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

    @Preview(showBackground = true, device = Devices.PIXEL_4)
    @OptIn(
        ExperimentalMaterial3Api::class,
    )
    @Composable
    fun ScaffoldWithTopBar() {
        val getReview by viewModel.reviewProduct.observeAsState()
        val id: ReviewComposeFragmentArgs by navArgs()
        viewModel.reviewItem(id.id)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getString(R.string.ulasan_pembeli),
                            style = TextStyle(
                                fontSize = 22.sp
                            )
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
            }
        ) {
            when (getReview) {
                is ResourcesResult.Loading -> {
                    Loading()
                }

                is ResourcesResult.Success -> {
                    val data =
                        (getReview as ResourcesResult.Success<ReviewResponse?>).data?.data
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(it)
                    ) {
                        LazyColumn {
                            items(data?.size ?: 0) { person ->
                                val name = data?.get(person)?.userName
                                val image = data?.get(person)?.userImage
                                val ratingUser = data?.get(person)?.userRating
                                val reviewUser = data?.get(person)?.userReview
                                Divider()
                                Column(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        AsyncImage(
                                            model = image,
                                            error = painterResource(id = R.drawable.img_thumbnail_produk),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(start = 15.dp)
                                                .size(36.dp)
                                                .clip(CircleShape),
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .width(10.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "$name",
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                                            )
                                            Row {
                                                if (ratingUser != null) {
                                                    RatingBar(rating = ratingUser)
                                                }
                                            }
                                        }
                                    }
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        text = "$reviewUser",
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                                    )
                                }
                            }
                        }
                    }
                }

                is ResourcesResult.Failure -> {
                    ErrorStateCompose(errorCode = "Empty",
                        errorInfo = "Your requested data is unavailable",
                        resetClick = { viewModel.reviewItem(id.id) }
                    )
                }

                else -> {}
            }

        }
    }

    @Composable
    fun RatingBar(
        rating: Int,
        modifier: Modifier = Modifier
    ) {
        val ratingState by remember {
            mutableStateOf(rating)
        }
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            for (i in 1..5) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_product),
                    contentDescription = "star",
                    modifier = modifier
                        .size(17.dp),
                    tint = if (i <= ratingState) Color(0xFFFFD700) else Color.DarkGray
                )

            }
        }
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

