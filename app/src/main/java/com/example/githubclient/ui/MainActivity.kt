package com.example.githubclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.githubclient.data.GitHubUtils
import com.example.githubclient.R
import com.example.githubclient.data.SharedPrefs
import com.example.githubclient.databinding.ActivityMainBinding
import com.example.githubclient.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @Inject
    lateinit var gitHubUtils: GitHubUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (sharedPrefs.getAccessToken() == null) {
            navigateToLoginFragment()
        } else {
            navigateToUserFragment()
        }

    }


    fun authorize() {
        val intent = Intent(Intent.ACTION_VIEW, gitHubUtils.buildAuthGitHubUrl())
        startActivity(intent)
    }

    fun navigateToLoginFragment() {
        supportFragmentManager.beginTransaction().replace(
            binding.root.id, LoginFragment.getInstance()
        ).commit()
    }

    fun navigateToUserFragment() {
        supportFragmentManager.beginTransaction().replace(
            binding.root.id, UserFragment.getInstance()
        ).addToBackStack(LoginFragment.TAG).commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val code = gitHubUtils.getCodeFromUri(intent?.data)
        code?.also {
            viewModel.getAccessToken(code)
        }
    }

}