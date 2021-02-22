package com.example.githubclient.di

import android.content.Context
import com.example.githubclient.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    companion object {
        private const val API_BASE_URL = "https://api.github.com"
        private const val OAUTH_BASE_URL = "https://github.com"
    }

    @Provides
    fun provideGitHubApiService(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): GitHubApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build().create(GitHubApiService::class.java)
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor, errorInterceptor: ErrorInterceptor) : OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(errorInterceptor)
            .build()
    }

    @Provides
    fun provideAuthInterceptor(sharedPrefs: SharedPrefs) : AuthInterceptor {
        return AuthInterceptor(sharedPrefs)
    }

    @Provides
    fun provideErrorInterceptor() : ErrorInterceptor {
        return ErrorInterceptor()
    }

    @Provides
    fun provideGitHubOAuthService(sharedPrefs: SharedPrefs): GitHubOAuthService {
        return Retrofit.Builder()
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(AuthInterceptor(sharedPrefs)).build()
            )
            .baseUrl(OAUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubOAuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPrefs {
        return SharedPrefs(context)
    }
}