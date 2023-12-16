package com.capstone.laperinapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityMainBinding
import com.capstone.laperinapp.ui.scan.CameraActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermisionGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSIONS
    ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemActiveIndicatorColor = getColorStateList(R.color.white)

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navView.setupWithNavController(navController)

        binding.fabScan.setOnClickListener { onClickScan() }
    }

    private fun onClickScan() {
        if (!allPermisionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            val modal = ModalBottomSheetDialog()
            supportFragmentManager.let { modal.show(it, ModalBottomSheetDialog.TAG) }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
    }
}