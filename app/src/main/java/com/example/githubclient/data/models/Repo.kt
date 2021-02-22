package com.example.githubclient.data.models

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("issues_url") val issuesUrl: String
) {
}