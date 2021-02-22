package com.example.githubclient.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubclient.databinding.RepoViewHolderBinding
import com.example.githubclient.ui.models.RepoUIModel

class RepoAdapter : ListAdapter<RepoUIModel, RepoViewHolder>(ReposDiffUtils) {
    private var lastLoadedItemShown = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding =
            RepoViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
        if(position == currentList.lastIndex) {
            lastLoadedItemShown = true
        }
    }

    fun updateList(newList: List<RepoUIModel>) {
        submitList(newList)
        lastLoadedItemShown = false
    }

    fun isLastLoadedItemShown(): Boolean {
        return lastLoadedItemShown
    }
}

class RepoViewHolder(private val binding: RepoViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: RepoUIModel) {
        binding.repoModel = repo
    }
}

object ReposDiffUtils : DiffUtil.ItemCallback<RepoUIModel>() {
    override fun areItemsTheSame(oldRepo: RepoUIModel, newRepo: RepoUIModel): Boolean {
        return oldRepo.name == newRepo.name
    }

    override fun areContentsTheSame(oldRepo: RepoUIModel, newRepo: RepoUIModel): Boolean {
        return oldRepo == newRepo
    }

}