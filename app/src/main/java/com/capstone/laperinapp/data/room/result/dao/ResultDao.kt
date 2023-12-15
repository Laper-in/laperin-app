package com.capstone.laperinapp.data.room.result.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.laperinapp.data.room.result.entity.ScanResult

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ScanResult)

    @Query("SELECT * FROM scan_result")
    fun getAllResult(): LiveData<List<ScanResult>>

    @Query("DELETE FROM scan_result")
    fun deleteAllResult()

    @Delete
    fun deleteById(result: ScanResult)
}