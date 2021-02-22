package com.example.githubclient.ui.mappers

import com.example.githubclient.data.models.Repo
import com.example.githubclient.ui.models.RepoUIModel
import javax.inject.Inject

class RepoUIMapper @Inject constructor() {
    fun map(repos: List<Repo>) : List<RepoUIModel> {
        return repos.map { repo ->
            RepoUIModel(
                name = repo.name
            )
        }
    }
}