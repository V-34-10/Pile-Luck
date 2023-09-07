package com.pyramidal.luuck.ui.splash


import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivitySplashBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.ui.main.menu.MenuActivity
import com.pyramidal.luuck.ui.main.privacy.PrivacyActivity
import com.pyramidal.luuck.utils.HideUIConfigUtils
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class LoadingActivity : AppCompatActivity(), CoroutineScope {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    private lateinit var sharedPref: SharedPreferences
    private var currentOvalIndex = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        job = Job()
        HideUIConfigUtils.hideUINavigation(this)
        loadingNextActivity()
        startOvalAnimations()
    }

    private fun loadingNextActivity() {
        initSharedPreferences()
        val flag = sharedPref.getBoolean("flag_key", false)
        val hasEmail = sharedPref?.contains("email_key") ?: false
        val hasPhone = sharedPref?.contains("phone_key") ?: false

        Handler().postDelayed({
            if (!flag) {
                // run PrivacyActivity
                val go = Intent(this@LoadingActivity, PrivacyActivity::class.java)
                startActivity(go)
                val editor = sharedPref.edit()
                editor.putBoolean("flag_key", true)
                editor.apply()
            } else if (hasEmail || hasPhone) {
                // run MenuActivity
                val go = Intent(this@LoadingActivity, MenuActivity::class.java)
                startActivity(go)
            } else {
                // run LoginActivity
                val go = Intent(this@LoadingActivity, LoginActivity::class.java)
                startActivity(go)
            }
            finish()
        }, 3 * 1000.toLong())
    }

    private fun initSharedPreferences() {
        sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val flag = sharedPref.getBoolean("flag_key", false)
        val editor = sharedPref.edit()
        editor.putBoolean("flag_key", flag)
        editor.apply()
    }

    private fun startOvalAnimations() {
        animateOvalsSequentially(binding.progressBar.progressBarLayout)
    }

    @SuppressLint("DiscouragedApi")
    private fun animateOvalsSequentially(progressBarLayout: ViewGroup) {
        if (currentOvalIndex > 12) {
            return
        }

        val ovalId = resources.getIdentifier("oval$currentOvalIndex", "id", packageName)
        val ovalView = progressBarLayout.findViewById<View>(ovalId)

        val colorAnimator = createColorAnimator(ovalView)
        colorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                currentOvalIndex++
                animateOvalsSequentially(progressBarLayout) // run anim next ellipse
            }
        })

        colorAnimator.start()
    }

    private fun createColorAnimator(view: View): ValueAnimator {
        val colorFrom = resources.getColor(R.color.startColorGradient)
        val colorTo = resources.getColor(R.color.centerColorGradient)

        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimator.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            val ovalDrawable = view.background as GradientDrawable
            ovalDrawable.setColor(color)
        }
        colorAnimator.duration = 200
        return colorAnimator
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}