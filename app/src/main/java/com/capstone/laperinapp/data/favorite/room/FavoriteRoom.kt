package com.capstone.laperinapp.data.favorite.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.laperinapp.data.favorite.dao.FavoriteDao
import com.capstone.laperinapp.data.favorite.entity.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteRoom : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDao

//    companion object {
//        @Volatile
//        private var instance: FavoriteRoom? = null
//
//        fun getInstance(context: Context): FavoriteRoom =
//            instance ?: synchronized(this) {
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    FavoriteRoom::class.java, "favorites.db"
//                ).build()
//            }
//    }
}