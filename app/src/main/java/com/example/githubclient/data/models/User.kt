package com.example.githubclient.data.models

import com.google.gson.annotations.SerializedName

data class User(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
    val name: String,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("total_private_repos") val privateRepos: Int
) {
}