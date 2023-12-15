package com.capstone.laperinapp.di

import android.content.Context
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.retrofit.ApiConfig
import com.capstone.laperinapp.data.room.result.database.ScanDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val dbScan = ScanDatabase.getDatabase(context)
        val scanDao = dbScan.resultDao()
        return Repository.getInstance(apiService, pref, scanDao)
    }
}