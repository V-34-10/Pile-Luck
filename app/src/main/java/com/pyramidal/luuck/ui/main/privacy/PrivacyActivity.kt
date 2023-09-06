package com.pyramidal.luuck.ui.main.privacy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityPrivacyBinding
import com.pyramidal.luuck.databinding.ActivitySplashBinding
import com.pyramidal.luuck.ui.login.LoginActivity

class PrivacyActivity : AppCompatActivity() {
    private var mainLink: String = "https://www.google.com/"
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnPrivacy.setOnClickListener {
            binding.btnPrivacy.startAnimation(animation)
            runPrivacy()
        }
        binding.btnYes.setOnClickListener {
            binding.btnYes.startAnimation(animation)
            loadingLoginActivity()
        }
        binding.btnNo.setOnClickListener {
            binding.btnNo.startAnimation(animation)
            loadingLoginActivity()
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun runPrivacy() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mainLink))
        startActivity(intent)
    }

    private fun loadingLoginActivity() {
        // run LoginActivity
        val go = Intent(this@PrivacyActivity, LoginActivity::class.java)
        startActivity(go)
        finish()
    }
}