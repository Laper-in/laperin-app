package com.capstone.laperinapp.data.room.favorite.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.laperinapp.data.room.favorite.dao.FavoriteDao
import com.capstone.laperinapp.data.room.favorite.entity.Favorite

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
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return instance as FavoriteRoomDatabase
        }
    }
}