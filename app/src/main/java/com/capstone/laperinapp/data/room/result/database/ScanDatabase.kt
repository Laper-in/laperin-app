package com.capstone.laperinapp.data.room.result.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.laperinapp.data.room.result.dao.ResultDao
import com.capstone.laperinapp.data.room.result.entity.ScanResult

@Database(entities = [ScanResult::class], version = 1, exportSchema = false)
abstract class ScanDatabase : RoomDatabase() {

    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        private var INSTANCE: ScanDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ScanDatabase {
            if (INSTANCE == null) {
                synchronized(ScanDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ScanDatabase::class.java, "scan_database.db"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE as ScanDatabase
        }
    }
}