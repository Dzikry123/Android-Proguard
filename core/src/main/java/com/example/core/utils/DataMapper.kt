package com.example.core.utils

import com.example.core.data.db.entity.GithubDetailEntity
import com.example.core.data.db.entity.GithubEntity
import com.example.core.data.responses.ResponseDetailUserGithub
import com.example.core.data.responses.ResponseUserGithub
import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser

object DataMapper {

    fun mapResponsesItemToEntities(input: ResponseUserGithub.Item): GithubEntity.Item {
        return GithubEntity.Item(
            avatar_url = input.avatar_url,
            id = input.id,
            login = input.login
        )
    }


    fun mapResponsesToEntities(input: ResponseUserGithub): GithubEntity {
        return GithubEntity(
            incomplete_results = input.incomplete_results,
            items = input.items.map {
                GithubEntity.Item(
                    avatar_url = it.avatar_url,
                    id = it.id,
                    login = it.login
                )
            },
            total_count = input.total_count
        )
    }

    fun mapEntitiesToDomain(input: MutableList<GithubEntity.Item>): List<GithubUser.Item> =
        input.map {
                    GithubUser.Item(
                        avatar_url = it.avatar_url,
                        id = it.id,
                        login = it.login
                    )
        }

    // Konversi dari GithubEntity.Item ke GithubUser.Item
    private fun GithubEntity.Item.toGithubUserItem(): GithubUser.Item {
        return GithubUser.Item(
            avatar_url = this.avatar_url,
            id = this.id,
            login = this.login
        )
    }

    // Implementasi konversi dari GithubEntity ke GithubUser.Item
    fun GithubEntity.toGithubUserItem(input: GithubEntity.Item): GithubUser.Item {
        return input.toGithubUserItem()
    }

    // Implementasi konversi dari GithubEntity ke GithubUser.Item
    fun GithubEntity.toGithubUserItem(): GithubUser.Item {
        val item = this.items.firstOrNull() // Ambil item pertama dari daftar
        return item?.toGithubUserItem() ?: throw IllegalStateException("GithubEntity does not contain any items")
    }


    fun mapEntityItemToDomain(entityItem: GithubEntity.Item): GithubUser.Item {
        return GithubUser.Item(
            avatar_url = entityItem.avatar_url,
            id = entityItem.id,
            login = entityItem.login
        )
    }

    fun mapDomainToEntity(input: GithubUser.Item) = GithubEntity.Item(
                avatar_url = input.avatar_url,
                id = input.id,
                login = input.login
    )

//    Detail Response

    fun mapDetailResponse(input: List<ResponseDetailUserGithub>): List<GithubDetailEntity> {
        val githubList = ArrayList<GithubDetailEntity>()
        input.map {
            val github = GithubDetailEntity(
                avatar_url = it.avatar_url,
                bio = it.bio.toString(),
                blog = it.blog,
                company = it.company,
                created_at = it.created_at,
                email = it.email.toString(),
                events_url = it.events_url,
                followers = it.followers,
                followers_url = it.followers_url,
                following = it.following,
                following_url = it.following_url,
                gists_url = it.gists_url,
                gravatar_id = it.gravatar_id,
                hireable = it.hireable.toString(),
                html_url = it.html_url,
                id = it.id,
                location = it.location,
                login = it.login,
                name = it.name,
                node_id = it.node_id,
                organizations_url = it.organizations_url,
                public_gists = it.public_gists,
                public_repos = it.public_repos,
                received_events_url = it.received_events_url,
                repos_url = it.repos_url,
                site_admin = it.site_admin,
                starred_url = it.starred_url,
                subscriptions_url = it.subscriptions_url,
                twitter_username = it.twitter_username.toString(),
                type = it.type,
                updated_at = it.updated_at,
                url = it.url
            )
            githubList.add(github)
        }
        return githubList
    }

    fun mapDetailEntitiesToDetailDomain(input: List<GithubDetailEntity>): List<GithubDetailUser> {
        val githubList = ArrayList<GithubDetailUser>()
        input.map {
            val github = GithubDetailUser(
                avatar_url = it.avatar_url,
                bio = it.bio?.toString(),
                blog = it.blog,
                company = it.company,
                created_at = it.created_at,
                email = it.email.toString(),
                events_url = it.events_url,
                followers = it.followers,
                followers_url = it.followers_url,
                following = it.following,
                following_url = it.following_url,
                gists_url = it.gists_url,
                gravatar_id = it.gravatar_id,
                hireable = it.hireable.toString(),
                html_url = it.html_url,
                id = it.id,
                location = it.location,
                login = it.login,
                name = it.name,
                node_id = it.node_id,
                organizations_url = it.organizations_url,
                public_gists = it.public_gists,
                public_repos = it.public_repos,
                received_events_url = it.received_events_url,
                repos_url = it.repos_url,
                site_admin = it.site_admin,
                starred_url = it.starred_url,
                subscriptions_url = it.subscriptions_url,
                twitter_username = it.twitter_username.toString(),
                type = it.type,
                updated_at = it.updated_at,
                url = it.url
            )
            githubList.add(github)
        }
        return githubList
    }


}