package com.example.githubclient.data

import com.example.githubclient.AppConstants
import com.example.githubclient.data.models.Repo
import com.example.githubclient.data.models.User
import retrofit2.http.*

interface GitHubApiService {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("user")
    suspend fun getUserInfo(): User?

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("user/repos?per_page=${AppConstants.reposPerPage}")
    suspend fun getUserRepos(@Query("page") page: String): List<Repo>?
}