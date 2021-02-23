package com.example.githubclient.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:avatarUrl")
fun avatarFromUrl(view: ImageView, url: String?) {
    url?.also {
        Glide.with(view).load(it).into(view)
    }
}