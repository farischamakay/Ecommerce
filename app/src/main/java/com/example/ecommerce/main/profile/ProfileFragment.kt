package com.example.ecommerce.main.profile

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private  var _binding : FragmentProfileBinding ?= null
    private val binding get() = _binding!!

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardProfile.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Pilih Gambar")
            alertDialogBuilder.setItems(R.array.options_select_picture_array,
                DialogInterface.OnClickListener{
                    dialog, index ->
                    when (index) {
                        0 -> {
                            val REQUEST_IMAGE_CAPTURE = 1
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            try {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(requireContext(), "Tidak dapat mengambil gambar", Toast.LENGTH_LONG).show()
                            }
                        }

                        1 -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                    }
                })
            alertDialogBuilder.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}