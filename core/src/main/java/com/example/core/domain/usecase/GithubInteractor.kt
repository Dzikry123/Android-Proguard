package com.example.core.domain.usecase

import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser
import com.example.core.domain.repository.IGithubRepository
import com.example.core.utils.DataMapper
import javax.inject.Inject

class GithubInteractor @Inject constructor(private val repository: IGithubRepository) : GithubUseCase {

    override suspend fun getUserFromGithub(): MutableList<GithubUser.Item> {
        val githubEntityItems = repository.getUserFromGithub()
        return githubEntityItems.map { DataMapper.mapEntityItemToDomain(it) }.toMutableList()
    }

    override suspend fun searchUserFromGithub(username: String): GithubUser {
        val githubEntity = repository.searchUserFromGithub(username)
        return GithubUser(
            incomplete_results = githubEntity.incomplete_results,
            items = githubEntity.items.map { DataMapper.mapEntityItemToDomain(it) },
            total_count = githubEntity.total_count
        )
    }

    override suspend fun getDetailUserFromGithub(username: String): GithubDetailUser = repository.getDetailUserFromGithub(username)


    override suspend fun getFollowerUserFromGithub(username: String): MutableList<GithubUser.Item> {
        val followerEntities = repository.getFollowerUserFromGithub(username)
        return followerEntities.map { DataMapper.mapEntityItemToDomain(it) }.toMutableList()
    }

    override suspend fun getFollowingUserFromGithub(username: String): MutableList<GithubUser.Item> {
        val followingEntities = repository.getFollowingUserFromGithub(username)
        return followingEntities.map { DataMapper.mapEntityItemToDomain(it) }.toMutableList()
    }
}
