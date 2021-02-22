package com.example.githubclient.data.models

sealed class ScreenContent<out E, out C> {
    data class Content<C>(val content: C) : ScreenContent<C, Nothing>()
    data class Error<E>(val error: E) : ScreenContent<Nothing, E>()
    object Loading : ScreenContent<Nothing, Nothing>()
}