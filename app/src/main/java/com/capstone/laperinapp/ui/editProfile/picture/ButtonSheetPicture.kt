package com.capstone.laperinapp.ui.editProfile.picture

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
import com.capstone.laperinapp.ui.scan.CameraActivity
import com.capstone.laperinapp.ui.scan.PreviewActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ButtonSheetPicture : BottomSheetDialogFragment() {
                private lateinit var binding: LayoutBottomsheetBinding

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
                        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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
                        val intent = Intent(requireContext(), CameraActivity::class.java)
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
                    if (uri != null){
                        val intent = Intent(requireContext(), EditPictureActivity::class.java)
                        intent.putExtra(EditPictureActivity.EXTRA_URI, uri.toString())
                        startActivity(intent)
            dismiss()
        } else {
            Log.d(TAG, "photoPicker: No Image Selected")
        }
    }

    companion object {
        const val TAG = "ButtonSheetPicture"
    }
}