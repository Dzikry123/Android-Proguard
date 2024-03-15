package com.example.core.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.db.entity.GithubEntity
import com.example.core.domain.model.GithubUser

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(user: GithubEntity.Item)

    @Query("SELECT * FROM USER")
    fun loadAll(): LiveData<MutableList<GithubEntity.Item>>

    @Query("SELECT * FROM USER WHERE id LIKE :id LIMIT 1")
    fun findByid(id: Int): GithubEntity.Item?

    @Delete
    fun deleteFav(user: GithubEntity.Item)
}