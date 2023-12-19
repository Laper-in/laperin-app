package com.capstone.laperinapp.data.room.favorite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.laperinapp.data.room.favorite.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipes : Favorite)

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun findById(id: String): Favorite?

    @Delete
    fun delete(recipe: Favorite)
}