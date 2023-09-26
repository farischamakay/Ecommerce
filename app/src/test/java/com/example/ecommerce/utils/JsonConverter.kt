package com.example.ecommerce.utils


import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets


object JsonConverter {
    fun MockWebServer.q(filename : String, responseCode: Int) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(filename)
        val source = inputStream.let { inputStream?.source()?.buffer()  }
        source?.let {
            enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(source.readString(StandardCharsets.UTF_8))
            )
        }
    }
}