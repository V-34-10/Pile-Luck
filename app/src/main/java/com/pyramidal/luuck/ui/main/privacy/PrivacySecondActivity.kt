package com.pyramidal.luuck.ui.main.privacy

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityPrivacySecondBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity
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
            loadingMenuActivity()
        }
    }

    private fun loadingMenuActivity() {
        val go = Intent(this@PrivacySecondActivity, MenuActivity::class.java)
        startActivity(go)
        finish()
    }

    private fun loadingWebViewPrivacyActivity() {
        val go = Intent(this@PrivacySecondActivity, WebViewPrivacyActivity::class.java)
        startActivity(go)
        finish()
    }

}