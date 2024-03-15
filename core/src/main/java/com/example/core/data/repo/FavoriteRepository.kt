package com.example.core.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.core.data.db.FavDao
import com.example.core.data.db.FavDb
import com.example.core.data.db.entity.GithubEntity
import com.example.core.domain.model.GithubUser
import com.example.core.domain.repository.IFavoriteRepository
import com.example.core.utils.DataMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class FavoriteRepository @Inject constructor(@ApplicationContext private val context: Context): IFavoriteRepository {

    private val mFavDao: FavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavDb.getDatabase(context)
        mFavDao = db.FavDao()
    }
    override fun getAllFavUser(): LiveData<MutableList<GithubUser.Item>> {
        val resultLiveData = MediatorLiveData<MutableList<GithubUser.Item>>()

        resultLiveData.addSource(mFavDao.loadAll()) { entities ->
            val domainItems = DataMapper.mapEntitiesToDomain(entities)
            resultLiveData.value = domainItems.toMutableList()
        }

        return resultLiveData
    }
    override fun insert(fav: GithubUser.Item) {
        val entity = DataMapper.mapDomainToEntity(fav)
        executorService.execute { mFavDao.insertFav(entity) }
    }
    override fun delete(fav: GithubUser.Item) {
        val entity = DataMapper.mapDomainToEntity(fav)
        executorService.execute { mFavDao.deleteFav(entity) }
    }


    override suspend fun findById(id: Int): GithubUser.Item? {
        return withContext(Dispatchers.IO) {
            val entityItem = mFavDao.findByid(id)
            entityItem?.let { DataMapper.mapEntityItemToDomain(it) }
        }
    }

}

