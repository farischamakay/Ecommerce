package com.example.ecommerce.main.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.data.models.request.ProfileRequest
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.utils.PhotoUriManager
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var mediaUri: Uri? = null
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var photoUriManager: PhotoUriManager

    private fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            cursor.moveToFirst()

            val name = cursor.getString(nameIndex)
            val size = cursor.getLong(sizeIndex)

            val cacheDir = File(context.cacheDir, "camera_cache")
            cacheDir.mkdirs()

            val cacheFile = File(cacheDir, name)
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(cacheFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            return cacheFile
        }

        return null
    }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                this.mediaUri = photoUriManager.getLatestUri()
                Glide.with(this).load(mediaUri).into(binding.imgProfile)

                Log.d("URI_CAM", "Uri : $mediaUri")
            } else {
                Log.d("URI_CAM", "Photo capture failed")
            }
        }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { mediaUri ->
            if (mediaUri != null) {
                this.mediaUri = mediaUri
                Glide.with(this).load(mediaUri).into(binding.imgProfile)

                Log.d("PhotoPicker", "Selected URI: $mediaUri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoUriManager = PhotoUriManager(requireContext())

        binding.imgProfile.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Pilih Gambar")
            alertDialogBuilder.setItems(
                R.array.options_select_picture_array
            ) { _, index ->
                when (index) {
                    0 -> {
                        val mediaUri = photoUriManager.buildNewUri()
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri)
                        cameraLauncher.launch(cameraIntent)
                    }

                    1 -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                }
            }
            alertDialogBuilder.show()
        }

        binding.layoutUsername.editText?.doOnTextChanged { inputUsername, _, _, _ ->
            binding.btnGoHome.isEnabled = inputUsername.toString().isNotEmpty()
        }

        binding.btnGoHome.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val imageFile =
                File(mediaUri?.let { it1 -> uriToFile(requireContext(), it1)?.path } ?: "")

            if (imageFile.exists()) {
                val requestImage = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart =
                    MultipartBody.Part.createFormData("userImage", imageFile.name, requestImage)
                val userNamePart = MultipartBody.Part.createFormData("userName", username)

                val profileRequest = ProfileRequest(userImage = imagePart, userName = userNamePart)

                viewModel.updateProfile(profileRequest)
            }

        }

        viewModel.profileResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResourcesResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_LONG).show()
                }

                is ResourcesResult.Success -> {
                    viewModel.saveUserName(binding.txtUsername.text.toString())
                    Toast.makeText(requireContext(), "Berhasil Upload!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is ResourcesResult.Failure -> {
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}