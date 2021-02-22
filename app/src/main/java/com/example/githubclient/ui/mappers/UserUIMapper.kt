package com.example.githubclient.ui.mappers

import com.example.githubclient.data.models.User
import com.example.githubclient.ui.models.UserUIModel
import javax.inject.Inject

class UserUIMapper @Inject constructor() {
    fun map(user: User): UserUIModel {
        return UserUIModel(
            fullName = user.name,
            userName = user.login,
            avatarUrl = user.avatarUrl
        )
    }
}