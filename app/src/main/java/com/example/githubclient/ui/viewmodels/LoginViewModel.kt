package com.example.githubclient.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclient.AppConstants
import com.example.githubclient.data.exceptions.InvalidNetworkResponseException
import com.example.githubclient.data.*
import com.example.githubclient.data.models.AccessToken
import com.example.githubclient.data.models.NetworkError
import com.example.githubclient.data.models.ScreenContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val gitHubService: GitHubOAuthService,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    private val _accessToken = MutableLiveData<ScreenContent<AccessToken, NetworkError>>()
    val accessToken = _accessToken as LiveData<ScreenContent<AccessToken, NetworkError>>

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is HttpException -> {
                _accessToken.postValue(ScreenContent.Error(NetworkError.RequestError))
            }
            is InvalidNetworkResponseException -> {
                _accessToken.postValue(ScreenContent.Error(NetworkError.InvalidNetworkResponse))
            }
            else -> {
                _accessToken.postValue(ScreenContent.Error(NetworkError.Unknown))
            }
        }
    }

    fun getAccessToken(code: String) {
        viewModelScope.launch(coroutineExceptionHandler + Dispatchers.IO) {
            val accessToken =
                gitHubService.getAccessToken(AppConstants.clientId, AppConstants.clientSecret, code)
            accessToken?.also {
                sharedPrefs.storeAccessToken("${it.tokenType} ${it.accessToken}")
                _accessToken.postValue(ScreenContent.Content(it))
            } ?: run {
                throw InvalidNetworkResponseException()
            }
        }
    }
}