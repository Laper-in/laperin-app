package com.capstone.laperinapp.data.favorite.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.laperinapp.data.favorite.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipes :Favorite)

    @Query("SELECT * FROM favorites")
    fun loadAll(): List<Favorite>

    @Query("SELECT * FROM favorites WHERE name LIKE :name LIMIT 1")
    fun findById(name: String): Favorite?

    @Delete
    fun delete(recipe: Favorite)
}