package com.example.core.domain.usecase

import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser

interface GithubUseCase {
    suspend fun getUserFromGithub(): MutableList<GithubUser.Item>
    suspend fun searchUserFromGithub(username: String): GithubUser

    suspend fun getDetailUserFromGithub(username: String): GithubDetailUser
    suspend fun getFollowerUserFromGithub(username: String): MutableList<GithubUser.Item>
    suspend fun getFollowingUserFromGithub(username: String): MutableList<GithubUser.Item>
}