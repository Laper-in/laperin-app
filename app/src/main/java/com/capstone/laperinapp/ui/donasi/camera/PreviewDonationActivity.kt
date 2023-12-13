package com.capstone.laperinapp.ui.donasi.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.capstone.laperinapp.databinding.ActivityPreviewDonationBinding
import com.capstone.laperinapp.ui.donasi.add.AddDonasiActivity
import java.io.IOException

class PreviewDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewDonationBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getImage()

        binding.btnLanjutkan.setOnClickListener { onClickLanjutkan() }
    }

    private fun onClickLanjutkan() {
        val intent = Intent(this, AddDonasiActivity::class.java)
        intent.putExtra(AddDonasiActivity.EXTRA_URI, currentImageUri.toString())
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (
            requestCode == REQUEST_CODE &&
            resultCode == RESULT_OK
        ) {
            currentImageUri = data?.data
            showImage()
        }
    }

    @Suppress("DEPRECATION")
    private fun getImage() {
        val imageUri = intent.getStringExtra(EXTRA_URI)
        currentImageUri = imageUri?.toUri()
        showImage()
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val exifInterface = ExifInterface(inputStream!!)

                val rotation = when (exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90,
                    ExifInterface.ORIENTATION_ROTATE_270 -> 90

                    else -> 0
                }

                val scaledType = if (rotation == 0) {
                    ImageView.ScaleType.FIT_CENTER
                } else {
                    ImageView.ScaleType.CENTER_CROP
                }

                binding.previewImage.scaleType = scaledType

                val rotatedBitmap = rotateBitmap(uri, rotation)
                binding.previewImage.setImageBitmap(rotatedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "showImage: ${e.message}")
            }
        }
    }

    private fun rotateBitmap(uri: Uri, rotation: Int): Bitmap {
        val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        return Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalBitmap.width,
            originalBitmap.height,
            matrix,
            true
        )
    }

    companion object {
        const val EXTRA_URI = "extra_camerax_image"
        const val TAG = "PreviewActivity"
        private const val REQUEST_CODE = 123
    }
}