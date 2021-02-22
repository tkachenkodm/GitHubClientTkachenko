package com.example.githubclient.data

import com.example.githubclient.data.models.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface GitHubOAuthService {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ) : AccessToken?
}