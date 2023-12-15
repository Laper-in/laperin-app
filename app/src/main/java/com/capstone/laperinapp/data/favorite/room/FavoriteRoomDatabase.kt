package com.capstone.laperinapp.data.favorite.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.laperinapp.data.favorite.dao.FavoriteDao
import com.capstone.laperinapp.data.favorite.entity.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteRoomDatabase : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDao

    companion object {
        @Volatile
        private var instance: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavoriteRoomDatabase {
            if (instance == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            FavoriteRoomDatabase::class.java, "recipesfavorite.db"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return instance as FavoriteRoomDatabase
        }
    }
}