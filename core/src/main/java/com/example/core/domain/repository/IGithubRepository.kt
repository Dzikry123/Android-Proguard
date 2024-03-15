package com.example.core.domain.repository

import com.example.core.data.db.entity.GithubEntity
import com.example.core.data.responses.ResponseDetailUserGithub
import com.example.core.data.responses.ResponseUserGithub
import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser

// GithubUserRepository
interface IGithubRepository {
    suspend fun getUserFromGithub(): MutableList<GithubEntity.Item>
    suspend fun searchUserFromGithub(username: String): GithubEntity

    suspend fun getDetailUserFromGithub(username: String): GithubDetailUser
    suspend fun getFollowerUserFromGithub(username: String): MutableList<GithubEntity.Item>
    suspend fun getFollowingUserFromGithub(username: String): MutableList<GithubEntity.Item>
}
