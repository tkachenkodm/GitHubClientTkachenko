package com.example.githubclient.data

import com.example.githubclient.data.exceptions.UserProfileNetworkException
import com.example.githubclient.data.exceptions.UserReposNetworkException
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException

class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {
        val currentRequest = chain.request()
        try {
            return chain.proceed(currentRequest)
        } catch (httpException: HttpException) {
            if (currentRequest.url().host().endsWith("user") && currentRequest.method() == "GET") {
                throw UserProfileNetworkException()
            } else if (currentRequest.url().host()
                    .endsWith("user/repos") && currentRequest.method() == "GET"
            ) {
                throw UserReposNetworkException()
            } else {
                throw httpException
            }
        }
    }
}