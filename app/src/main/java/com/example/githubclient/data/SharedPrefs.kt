package com.example.githubclient.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefs @Inject constructor(context: Context) {
    private companion object {
        const val TOKEN_KEY = "TOKEN"
        const val sharedPrefsName = "app_shared_prefs"
    }

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPrefsName, MODE_PRIVATE)
    }

    fun getAccessToken(): String? {
        return sharedPrefs.getString(TOKEN_KEY, null)
    }

    fun storeAccessToken(token: String) {
        sharedPrefs.edit().putString(TOKEN_KEY, token).apply()
    }

}