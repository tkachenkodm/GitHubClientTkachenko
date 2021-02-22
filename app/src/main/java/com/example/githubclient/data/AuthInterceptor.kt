package com.example.githubclient.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sharedPrefs: SharedPrefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getAccessToken()
        val currentRequest = chain.request()

        if (token != null) {
            val newRequest = currentRequest.newBuilder().header(
                "Authorization",
                token
            ).build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(currentRequest)
    }
}