package com.example.core.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class GithubUser (
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
) {

    @Parcelize
    data class Item(

        val avatar_url: String,
        val id: Int,
        val login: String

    ) : Parcelable

}
