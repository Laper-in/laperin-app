package com.capstone.laperinapp.ui.donasi.camera

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.google.android.material.R
import com.capstone.laperinapp.databinding.LayoutBottomsheetBinding
import com.capstone.laperinapp.ui.donasi.add.AddDonasiActivity
import com.capstone.laperinapp.ui.edit.picture.OnImageSelectedListener
import com.capstone.laperinapp.ui.scan.CameraActivity
import com.capstone.laperinapp.ui.scan.PreviewActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheetDonationDialog : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomsheetBinding
    private var onImageResultListener: OnImageResultListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomsheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as Dialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
                behavior.isHideable = false
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnCamera.setOnClickListener {
            val intent = Intent(requireContext(), CameraDonationActivity::class.java)
            launcherIntentCameraX.launch(intent)
            dismiss()
        }
        
        binding.btnGalery.setOnClickListener {
            startGallery()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val uri = it.data?.getStringExtra(AddDonasiActivity.EXTRA_URI)?.toUri()
            onImageResultListener?.onImageResult(uri!!)
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null){
            onImageResultListener?.onImageResult(uri)
            Toast.makeText(requireContext(), "$uri", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "uri: $uri")
            dismiss()
        } else {
            Log.d(TAG, "photoPicker: No Image Selected")
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}