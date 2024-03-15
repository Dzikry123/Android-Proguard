package com.example.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.data.db.entity.GithubEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [GithubEntity.Item::class], version = 2)
abstract class FavDb : RoomDatabase() {
    abstract fun FavDao(): FavDao
    companion object {
        @Volatile
        private var INSTANCE: FavDb? = null
        @JvmStatic
        fun getDatabase(context: Context): FavDb {
            val passphrase: ByteArray = SQLiteDatabase.getBytes("myFavDb".toCharArray())
            val factory = SupportFactory(passphrase)

            if (INSTANCE == null) {
                synchronized(FavDb::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavDb::class.java, "fav_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as FavDb
        }
    }
}