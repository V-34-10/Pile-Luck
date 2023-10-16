package com.pyramidal.luuck.ui.main.privacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityPrivacyBinding
import com.pyramidal.luuck.databinding.ActivityPrivacySecondBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.ui.utils.HideUIConfigUtils

class PrivacySecondActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPrivacySecondBinding.inflate(layoutInflater) }
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
            loadingWebViewPrivacyActivity()
        }
        binding.btnAccept.setOnClickListener {
            binding.btnAccept.startAnimation(animation)
            loadingLoginActivity()
        }
    }

    private fun loadingLoginActivity() {
        val go = Intent(this@PrivacySecondActivity, LoginActivity::class.java)
        startActivity(go)
        finish()
    }

    private fun loadingWebViewPrivacyActivity() {
        val go = Intent(this@PrivacySecondActivity, WebViewPrivacyActivity::class.java)
        startActivity(go)
        finish()
    }

}