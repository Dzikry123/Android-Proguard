package com.example.core.data.repo

import android.util.Log
import com.example.core.data.db.entity.GithubEntity
import com.example.core.domain.repository.IGithubRepository
import com.example.core.data.responses.ResponseDetailUserGithub
import com.example.core.data.responses.ResponseUserGithub
import com.example.core.data.service.ServiceGithub
import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser
import com.example.core.utils.DataMapper
import javax.inject.Inject

// UserRepository.kt
class GithubRepository @Inject constructor(private val  serviceGithub: ServiceGithub) :
    IGithubRepository {
    // Metode getUserFromGithub tetap menggunakan GithubEntity.Item
    override suspend fun getUserFromGithub(): MutableList<GithubEntity.Item> {
        val response = serviceGithub.getUserFromGithub()
        return response.map { DataMapper.mapResponsesItemToEntities(it) }.toMutableList()
    }

    // Metode searchUserFromGithub tetap menggunakan GithubEntity
    override suspend fun searchUserFromGithub(username: String): GithubEntity {
        val response = serviceGithub.searchUserFromGithub(mapOf("q" to username, "per_page" to 10))
        return DataMapper.mapResponsesToEntities(response)
    }


    override suspend fun getDetailUserFromGithub(username: String): GithubDetailUser {
        val response = serviceGithub.getDetailUserFromGithub(username)
        return response.toGithubDetailUser()
    }


    // Metode getFollowerUserFromGithub menggunakan GithubEntity.Item
    override suspend fun getFollowerUserFromGithub(username: String): MutableList<GithubEntity.Item> {
        val response = serviceGithub.getFollowerUserFromGithub(username)
        return response.map { DataMapper.mapResponsesItemToEntities(it) }.toMutableList()
    }

    // Metode getFollowingUserFromGithub menggunakan GithubEntity.Item
    override suspend fun getFollowingUserFromGithub(username: String): MutableList<GithubEntity.Item> {
        val response = serviceGithub.getFollowingUserFromGithub(username)
        return response.map { DataMapper.mapResponsesItemToEntities(it) }.toMutableList()
    }

//    // Ekstensi untuk konversi ResponseUserGithub.Item ke GithubUser.Item
//    private fun ResponseUserGithub.Item.toGithubUserItem(): GithubUser.Item {
//        return GithubUser.Item(
//            avatar_url = this.avatar_url,
//            id = this.id,
//            login = this.login
//        )
//    }
//
//    // Ekstensi untuk konversi ResponseUserGithub ke GithubUser
//    private fun ResponseUserGithub.toGithubUser(): GithubUser {
//        return GithubUser(
//            incomplete_results = this.incomplete_results,
//            items = this.items.map { it.toGithubUserItem() },
//            total_count = this.total_count
//        )
//    }

    // Ekstensi untuk konversi ResponseDetailUserGithub ke GithubDetailUser
    private fun ResponseDetailUserGithub.toGithubDetailUser(): GithubDetailUser {
        Log.d("Debugging", "bio: ${this.bio}")
        Log.d("Debugging", "email: ${this.email}")
        Log.d("Debugging", "hireable: ${this.hireable}")
        Log.d("Debugging", "twitter_username: ${this.twitter_username}")

        return GithubDetailUser(
            avatar_url = this.avatar_url,
            bio = this.bio.toString(),
            blog = this.blog,
            company = this.company,
            created_at = this.created_at,
            email = this.email.toString(),
            events_url = this.events_url,
            followers = this.followers,
            followers_url = this.followers_url,
            following = this.following,
            following_url = this.following_url,
            gists_url = this.gists_url,
            gravatar_id = this.gravatar_id,
            hireable = this.hireable.toString(),
            html_url = this.html_url,
            id = this.id,
            location = this.location,
            login = this.login,
            name = this.name,
            node_id = this.node_id,
            organizations_url = this.organizations_url,
            public_gists = this.public_gists,
            public_repos = this.public_repos,
            received_events_url = this.received_events_url,
            repos_url = this.repos_url,
            site_admin = this.site_admin,
            starred_url = this.starred_url,
            subscriptions_url = this.subscriptions_url,
            twitter_username = this.twitter_username.toString(),
            type = this.type,
            updated_at = this.updated_at,
            url = this.url
        )
    }


}

