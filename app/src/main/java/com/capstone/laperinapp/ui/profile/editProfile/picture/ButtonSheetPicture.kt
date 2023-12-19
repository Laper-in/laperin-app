package com.capstone.laperinapp.ui.profile.editProfile.picture

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.capstone.laperinapp.databinding.LayoutBottomsheetBinding
import com.capstone.laperinapp.ui.donasi.add.OnImageSelectedListener
import com.capstone.laperinapp.ui.donasi.camera.ModalBottomSheetDonationDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class ButtonSheetPicture : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomsheetBinding
    interface OnImageSelectedListener {
        fun onImageSelected(uri: Uri)
    }

    private var onImageSelectedListener: OnImageSelectedListener? = null
    fun setOnImageSelectedListener(listener: OnImageSelectedListener) {
        onImageSelectedListener = listener
    }
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
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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
            val intent = Intent(requireContext(), CameraPictureActivity::class.java)
            startActivity(intent)
            dismiss()
        }

        binding.btnGalery.setOnClickListener {
            startGallery()
        }
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onImageSelectedListener?.onImageSelected(uri)
            dismiss()
        } else {
            Log.d(TAG, "photoPicker: No Image Selected")
        }
    }

    companion object {
        const val TAG = "ButtonSheetPicture"
    }
}