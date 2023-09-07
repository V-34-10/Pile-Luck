package com.pyramidal.luuck.ui.main.privacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityPrivacyBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.utils.HideUIConfigUtils

class PrivacyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPrivacyBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnPrivacy.setOnClickListener {
            binding.btnPrivacy.startAnimation(animation)
            RemotePrivacy.openPrivacyLink(this)
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

    private fun loadingLoginActivity() {
        // run LoginActivity
        val go = Intent(this@PrivacyActivity, LoginActivity::class.java)
        startActivity(go)
        finish()
    }
}