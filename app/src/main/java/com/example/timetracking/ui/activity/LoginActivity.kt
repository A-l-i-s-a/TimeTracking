package com.example.timetracking.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.timetracking.MainActivity
import com.example.timetracking.R
import com.example.timetracking.repository.login.LoginRequest
import com.example.timetracking.ui.loader.ProgressBarLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var loader: ProgressBarLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loader = ProgressBarLoader(
            context = this,
            progressBar = pbLoader,
            rootView = rootLoginActivity,
            anchorView = null
        )
        loader.setReplaceableView(btnSignIn)
        loader.setSuccessEvent {
            if (cbSaveMe.isChecked) {
                viewModel.saveUser(
                    tfLogin.editText!!.text.toString(),
                    tfPassword.editText?.text.toString()
                )
            }
            // успешная авторизация
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Тестовые значения
        // "mobile"
        // "12345678bb"
        viewModel.getSaveUser {
            etLogin.setText(it.login ?: "")
            etPassword.setText(it.password ?: "")
        }

        btnSignIn.setOnClickListener {
            viewModel.login(
                LoginRequest(
                    etLogin.text.toString(),
                    etPassword.text.toString()
                ),
                loading = { loader.isLoading(loading = true, loadContent = false) },
                success = { loader.isLoading(loading = false, loadContent = true)},
                error = { loader.errorEvent(it) }
            )
        }
    }
}