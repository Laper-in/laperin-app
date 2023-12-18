package com.capstone.laperinapp.ui.scan.preview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivityPreviewBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ml.Mobilev2Data498
import com.capstone.laperinapp.ui.scan.result.ResultActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
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

    private val viewModel by viewModels<PreviewViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getImage()
        setupToolbar()

        labels = application.assets.open("labels.txt").bufferedReader().readLines()

        binding.btnLanjutkan.setOnClickListener { onClickLanjutkan() }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Scan"
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(TextAppearanceSpan(this, R.style.textColorDonasi), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = spannableTitle
        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        backIcon?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    private fun imageProcessor() {
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    private fun onClickLanjutkan() {
        val result = ScanResult(
            0,
            predictionImage()
        )
        viewModel.insertResult(result)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULT, currentImageUri.toString())
        startActivity(intent)
        finish()
    }

    private fun predictionImage(): String {

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        val model = Mobilev2Data498.newInstance(this)
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)
        inputFeature0.buffer.rewind()
        val inputBuffer = inputFeature0.floatArray
        for (i in inputBuffer.indices) {
            inputBuffer[i] = inputBuffer[i].coerceIn(0.0f, 1.0f)
        }
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxIdxFeature0 = 0
        outputFeature0.forEachIndexed { index, fl ->
            if (outputFeature0[maxIdxFeature0] < fl) {
                maxIdxFeature0 = index
            }
        }

        model.close()

        return labels[maxIdxFeature0]
    }

    @Suppress("DEPRECATION")
    private fun getImage() {
        val imageUri = intent.getStringExtra(EXTRA_URI)
        currentImageUri = imageUri?.toUri()
        showImage()
        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUri)
        imageProcessor()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_URI = "extra_camerax_image"
        const val TAG = "PreviewActivity"
    }
}