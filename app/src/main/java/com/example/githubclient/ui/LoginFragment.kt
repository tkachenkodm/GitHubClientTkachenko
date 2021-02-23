package com.example.githubclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.githubclient.data.GitHubUtils
import com.example.githubclient.R
import com.example.githubclient.data.models.ScreenContent
import com.example.githubclient.databinding.LoginFragmentBinding
import com.example.githubclient.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var binding: LoginFragmentBinding
    @Inject lateinit var gitHubUtils: GitHubUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.login_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        subscribeToLiveData()
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.accessToken.observe( viewLifecycleOwner, { content ->
            if (content is ScreenContent.Content) {
                (activity as MainActivity).navigateToUserFragment()
            } else {
                Toast.makeText(context, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun login() {
        (activity as MainActivity).authorize()
    }

    companion object {
        const val TAG = "LoginFragment"

        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}