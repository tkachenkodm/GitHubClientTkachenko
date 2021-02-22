package com.example.githubclient.ui.viewmodels

import androidx.lifecycle.*
import com.example.githubclient.AppConstants
import com.example.githubclient.data.exceptions.InvalidNetworkResponseException
import com.example.githubclient.data.exceptions.UserProfileNetworkException
import com.example.githubclient.data.exceptions.UserReposNetworkException
import com.example.githubclient.data.GitHubApiService
import com.example.githubclient.data.models.NetworkError
import com.example.githubclient.data.models.ScreenContent
import com.example.githubclient.ui.mappers.RepoUIMapper
import com.example.githubclient.ui.mappers.UserUIMapper
import com.example.githubclient.ui.models.RepoUIModel
import com.example.githubclient.ui.models.UserUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val gitHubService: GitHubApiService,
    private val userUIMapper: UserUIMapper,
    private val repoUIMapper: RepoUIMapper
) : ViewModel() {

    private val _user = MutableLiveData<ScreenContent<UserUIModel, NetworkError>>()
    val user = _user as LiveData<ScreenContent<UserUIModel, NetworkError>>

    private val _repos = MutableLiveData<List<ScreenContent<RepoUIModel, NetworkError>>>()
    var repos = _repos as LiveData<List<ScreenContent<RepoUIModel, NetworkError>>>

    private var repoPage = firstPage
    var totalRepos = 0


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    AppConstants.HTTP_NOT_AUTHORIZED -> {
                        _user.postValue(ScreenContent.Error(NetworkError.NotAuthorized))
                        _repos.postValue(listOf(ScreenContent.Error(NetworkError.NotAuthorized)))
                    }
                    else -> {
                        _user.postValue(ScreenContent.Error(NetworkError.RequestError))
                        _repos.postValue(listOf(ScreenContent.Error(NetworkError.RequestError)))
                    }
                }

            }
            is InvalidNetworkResponseException -> {
                _user.postValue(ScreenContent.Error(NetworkError.InvalidNetworkResponse))
                _repos.postValue(listOf(ScreenContent.Error(NetworkError.InvalidNetworkResponse)))
            }
            is UserProfileNetworkException -> {
                _user.postValue(ScreenContent.Error(NetworkError.InvalidNetworkResponse))
            }
            is UserReposNetworkException -> {
                _repos.postValue(listOf(ScreenContent.Error(NetworkError.InvalidNetworkResponse)))
            }
            else -> {
                _user.postValue(ScreenContent.Error(NetworkError.Unknown))
                _repos.postValue(listOf(ScreenContent.Error(NetworkError.Unknown)))
            }
        }
    }


    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _user.postValue(ScreenContent.Loading)
            val userResult = gitHubService.getUserInfo()
            userResult?.also { user ->
                totalRepos = user.publicRepos + user.privateRepos
                _user.postValue(ScreenContent.Content(userUIMapper.map(user)))
            } ?: run {
                throw InvalidNetworkResponseException()
            }
        }
    }

    fun getUserRepos(page: Int = firstPage) {
        viewModelScope.launch(Dispatchers.IO) {
            val reposResult = gitHubService.getUserRepos(page.toString())
            reposResult?.also { repos ->
                val reposData = repoUIMapper.map(repos).map {
                    ScreenContent.Content(it)
                }

                _repos.value?.also { existingRepos ->
                    val newReposData = existingRepos.toMutableList()
                    newReposData.also {
                        it.addAll(reposData)
                        _repos.postValue(it)
                    }
                } ?: run {
                    _repos.postValue(reposData)
                }
            } ?: run {
                throw UserReposNetworkException()
            }
        }

    }

    fun getNextReposPage() {
        repoPage++
        getUserRepos(repoPage)
    }

    companion object {
        private const val firstPage = 1
    }
}