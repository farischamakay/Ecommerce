package com.example.ecommerce.main.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.utils.PhotoUriManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoUriManager: PhotoUriManager

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = photoUriManager.getLatestUri()
                Glide.with(this).load(uri).into(binding.imgProfile)

                Log.d("URI_CAM", "Uri : $uri")
            } else {
                Log.d("URI_CAM", "Photo capture failed")
            }
        }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { mediaUri ->
            if (mediaUri != null) {
                val uri = photoUriManager.buildNewUri()
                photoUriManager.setLatestUri(mediaUri)
                Glide.with(this).load(uri).into(binding.imgProfile)

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
            alertDialogBuilder.setItems(R.array.options_select_picture_array,
                DialogInterface.OnClickListener { dialog, index ->
                    when (index) {
                        0 -> {
                            val uri = photoUriManager.buildNewUri()
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                            cameraLauncher.launch(cameraIntent)
                        }

                        1 -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                    }
                })
            alertDialogBuilder.show()
        }

        binding.layoutUsername.editText?.doOnTextChanged { inputUsername, _, _, _ ->
            binding.btnGoHome.isEnabled = inputUsername.toString().isNotEmpty()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}