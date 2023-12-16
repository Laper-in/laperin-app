package com.capstone.laperinapp.ui.profile.editProfile.picture

import android.app.Activity
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
import com.capstone.laperinapp.databinding.ActivityEditPictureBinding
import com.capstone.laperinapp.ui.profile.editProfile.EditProfilActivity
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.IOException
class EditPictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPictureBinding
    private var currentImageUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var imageProcessor: ImageProcessor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)


        imageProcessor()
        getImage()
        binding.btnLanjutkan.setOnClickListener {
            getPicture()
        }
    }
    @Suppress("DEPRECATION")
    private fun getImage() {
        val imageUri = intent.getStringExtra(EXTRA_URI)
        currentImageUri = imageUri?.toUri()
        showImage()
        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUri)
    }

    private fun imageProcessor() {
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun getPicture() {
        val resultIntent = Intent()
        resultIntent.putExtra(EditProfilActivity.EXTRA_PROFILE, currentImageUri.toString())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
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

    @Suppress("DEPRECATION")
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
        const val TAG = "EditPictureActivity"
    }
}