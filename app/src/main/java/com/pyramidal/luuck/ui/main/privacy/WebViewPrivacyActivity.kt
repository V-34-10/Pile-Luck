package com.pyramidal.luuck.ui.main.privacy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityPrivacySecondBinding
import com.pyramidal.luuck.databinding.ActivityWebViewPrivacyBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.ui.utils.HideUIConfigUtils

class WebViewPrivacyActivity : AppCompatActivity() {
    private lateinit var WebViewShow: WebView
    private var mainLink: String = "https://www.google.com/" //TODO change on release!!!
    private val binding by lazy { ActivityWebViewPrivacyBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        initWebView()
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnAccept.setOnClickListener {
            binding.btnAccept.startAnimation(animation)
            loadingLoginActivity()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        WebViewShow = findViewById(R.id.WebViewShow)
        WebViewShow.webViewClient = WebViewClient()
        WebViewShow.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowContentAccess = true
        }
        WebViewShow.webViewClient = WebViewClient()
        WebViewShow.loadUrl(mainLink)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (WebViewShow.canGoBack()) {
                        WebViewShow.goBack()
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun loadingLoginActivity() {
        val go = Intent(this@WebViewPrivacyActivity, PrivacySecondActivity::class.java)
        startActivity(go)
        finish()
    }
}