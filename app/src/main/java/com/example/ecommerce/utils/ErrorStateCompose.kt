package com.example.ecommerce.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R


@Composable
fun ErrorStateCompose(
    errorCode : String,
    errorInfo : String,
    resetClick : () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.img_error_result),
            modifier = Modifier
                .size(128.dp)
                .padding(5.dp),
            contentDescription = null)
        Text(
            text = errorCode,
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontWeight = FontWeight.Bold,
        )
        Text(text = errorInfo,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            modifier = Modifier)
        Button(onClick = {
            resetClick
        }) {
            Text(text = "Reset", fontFamily = FontFamily(Font(R.font.poppins_regular)))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview(){
    ErrorStateCompose(errorCode = "Empty", errorInfo = "Your requested data is unavailable") {

    }
}