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
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.capstone.laperinapp.databinding.ActivityEditPictureBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.profile.ProfileFragment
import com.capstone.laperinapp.ui.profile.ProfileViewModel
import com.capstone.laperinapp.ui.profile.editProfile.EditProfilActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.File
import java.io.IOException
class EditPictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPictureBinding
    private var currentImageUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var imageProcessor: ImageProcessor
    private var selectedImageUri: Uri? = null

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
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

    private fun updateImageUser() {
        if (selectedImageUri != null) {
            val file = File(selectedImageUri!!.path)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            viewModel.updateImageUser(imagePart).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, ProfileFragment::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {

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
        selectedImageUri = currentImageUri
        showImage()
        updateImageUser()
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
    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    companion object {
        const val EXTRA_URI = "extra_camerax_image"
        const val TAG = "EditPictureActivity"
    }
}