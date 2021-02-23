package com.example.githubclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubclient.R
import com.example.githubclient.data.models.NetworkError
import com.example.githubclient.data.models.ScreenContent
import com.example.githubclient.databinding.UserInfoFragmentBinding
import com.example.githubclient.ui.models.RepoUIModel
import com.example.githubclient.ui.models.UserUIModel
import com.example.githubclient.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: UserInfoFragmentBinding
    private lateinit var adapter: RepoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.user_info_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setRecyclerScrollListener()
        subscribeToUserData()
        subscribeToReposData()
        viewModel.getUserInfo()
    }

    private fun setAdapter() {
        adapter = RepoAdapter()
        binding.rvRepos.adapter = adapter
        binding.rvRepos.layoutManager = LinearLayoutManager(context)
    }

    private fun setRecyclerScrollListener() {
        binding.rvRepos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.repos.value?.also {
                    if (!adapter.isLastLoadedItemShown() && viewModel.totalRepos >= it.lastIndex && dy > 0) {
                        binding.piLoading.visibility = View.VISIBLE
                        viewModel.getNextReposPage()
                    }
                }
            }
        })
    }

    private fun subscribeToUserData() {
        viewModel.user.observe(viewLifecycleOwner) { content ->
            when (content) {
                is ScreenContent.Content -> {
                    binding.piLoading.visibility = View.GONE
                    showUser(content.content)
                    viewModel.getUserRepos()
                }
                is ScreenContent.Error -> {
                    binding.piLoading.visibility = View.GONE
                    if (content.error == NetworkError.NotAuthorized) {
                        (activity as MainActivity).navigateToLoginFragment()
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.loading_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                is ScreenContent.Loading -> {
                    binding.piLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun subscribeToReposData() {
        viewModel.repos.observe(viewLifecycleOwner) { content ->
            binding.piLoading.visibility = View.GONE
            val loadedRepos = content.filter { it is ScreenContent.Content }
                .map { (it as ScreenContent.Content).content }
            if (content.any { it is ScreenContent.Error }) {
                Toast.makeText(context, getString(R.string.some_repos_error), Toast.LENGTH_SHORT)
                    .show()
            }
            showRepos(loadedRepos)
        }
    }

    private fun showUser(user: UserUIModel) {
        binding.userModel = user
    }

    private fun showRepos(repos: List<RepoUIModel>) {
        adapter.updateList(repos)
    }

    companion object {
        const val TAG = "UserFragment"

        fun getInstance(): UserFragment {
            return UserFragment()
        }
    }
}