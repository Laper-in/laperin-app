package com.capstone.laperinapp.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.databinding.ActivityMainBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.ui.home.HomeFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navView.setupWithNavController(navController)
    }
}