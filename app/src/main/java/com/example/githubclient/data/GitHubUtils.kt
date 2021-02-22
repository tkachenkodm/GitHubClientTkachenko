package com.example.githubclient.data

import android.net.Uri
import com.example.githubclient.AppConstants
import com.example.githubclient.AppConstants.host
import com.example.githubclient.AppConstants.schema
import com.example.githubclient.AppConstants.scopes
import javax.inject.Inject


class GitHubUtils @Inject constructor() {

    fun buildAuthGitHubUrl(): Uri {
        return Uri.Builder()
            .scheme(schema)
            .authority(host)
            .appendEncodedPath("login/oauth/authorize")
            .appendQueryParameter("client_id", AppConstants.clientId)
            .appendQueryParameter("scope", scopes)
            .appendQueryParameter("redirect_url", AppConstants.redirectUri)
            .build()
    }

    fun getCodeFromUri(uri: Uri?): String? {
        uri ?: return null
        if (!uri.toString().startsWith(AppConstants.redirectUri)) {
            return null
        }
        return uri.getQueryParameter("code")
    }
}