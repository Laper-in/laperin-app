package com.capstone.laperinapp.ui.scan

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
import android.widget.Toast
import androidx.core.net.toUri
import com.capstone.laperinapp.databinding.ActivityPreviewBinding
import com.capstone.laperinapp.ml.Resnet50Coba2
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding

    private var currentImageUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var imageProcessor: ImageProcessor
    private lateinit var labels: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getImage()
        imageProcessor()

        labels = application.assets.open("labels.txt").bufferedReader().readLines()

        binding.btnLanjutkan.setOnClickListener { predictionImage() }
    }

    private fun imageProcessor() {
       imageProcessor = ImageProcessor.Builder()
//           .add(NormalizeOp(0f, 255f))
//           .add(TransformToGrayscaleOp())
            .add(ResizeOp(640, 640, ResizeOp.ResizeMethod.BILINEAR))
            .build()
    }

    private fun onClickLanjutkan() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULT, currentImageUri.toString())
        startActivity(intent)
        finish()
    }

    private fun predictionImage() {

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        val model = Resnet50Coba2.newInstance(this)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 640, 640, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        val outputFeature1 = outputs.outputFeature1AsTensorBuffer.floatArray
        val outputFeature2 = outputs.outputFeature2AsTensorBuffer.floatArray
        val outputFeature3 = outputs.outputFeature3AsTensorBuffer.floatArray

        var maxIdxFeature0 = 0
        outputFeature0.forEachIndexed { index, fl ->
            if (outputFeature0[maxIdxFeature0] < fl) {
                maxIdxFeature0 = index
            }
        }

        Log.i(TAG, "predictionImage: ${labels[maxIdxFeature0]}")
        Toast.makeText(this, labels[maxIdxFeature0], Toast.LENGTH_SHORT).show()

        model.close()
    }

    @Suppress("DEPRECATION")
    private fun getImage() {
        val imageUri = intent.getStringExtra(EXTRA_URI)
        currentImageUri = imageUri?.toUri()
        showImage()
        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUri)
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
    }
}