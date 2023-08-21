package com.example.ecommerce.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class PhotoUriManager(private val appContext: Context) {

    private var latestUri: Uri? = null
    private lateinit var pickerUri: Uri

    fun buildNewUri(): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${appContext.packageName}.$FILE_PROVIDER"
        latestUri = FileProvider.getUriForFile(appContext, authority, photoFile)
        return latestUri!!
    }

    fun getLatestUri(): Uri? {
        return latestUri
    }

    fun buildUriPicker(): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${appContext.packageName}.$FILE_PROVIDER"
        pickerUri = FileProvider.getUriForFile(appContext, authority, photoFile)
        return latestUri!!
    }

    fun setLatestUri(uri: Uri): Uri {
        return pickerUri
    }

    /**
     * Create a unique file name based on the time the photo is taken
     */
    private fun generateFilename() = "selfie-${System.currentTimeMillis()}.jpg"

    companion object {
        private const val PHOTOS_DIR = "photos"
        private const val FILE_PROVIDER = "fileprovider"
    }
}