package com.pyramidal.luuck.ui.main.settings

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.SettingsActivityBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { SettingsActivityBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private var isAnimating = false
    private var currentOvalIndex = 1
    private lateinit var audioManager: AudioManager
    private lateinit var vibrator: Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        startStateBar()
        controlButton()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnBack.setOnClickListener {
            binding.btnBack.startAnimation(animation)
            // run MenuActivity
            val go = Intent(this@SettingsActivity, MenuActivity::class.java)
            startActivity(go)
            finish()
        }

        binding.textRemoveAccount.setOnClickListener {
            binding.textRemoveAccount.startAnimation(animation)
            sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
        }
        binding.textResetScore.setOnClickListener {
            binding.textResetScore.startAnimation(animation)
            //TODO score clean
        }
        binding.soundBar.progressBarLayout.setOnClickListener {
            if (isAnimating) {
                stopOvalAnimations(binding.soundBar.progressBarLayout)
                decreaseVolume()
            } else {
                startOvalAnimations(binding.soundBar.progressBarLayout)
                increaseVolume()
            }
        }
        binding.vibrationBar.progressBarLayout.setOnClickListener {
            if (isAnimating) {
                stopOvalAnimations(binding.vibrationBar.progressBarLayout)
                decreaseVibration()
            } else {
                startOvalAnimations(binding.vibrationBar.progressBarLayout)
                increaseVibration()
            }
        }
    }

    private fun startStateBar() {
        val ovalIds = arrayOf(
            R.id.oval1, R.id.oval2, R.id.oval3, R.id.oval4, R.id.oval5, R.id.oval6
        )

        for (i in 0 until 6) {
            val ovalId = ovalIds[i]
            val ovalView = findViewById<View>(ovalId)
            val ovalDrawable = ovalView.background as GradientDrawable
            ovalDrawable.setColor(resources.getColor(R.color.startColorGradient))
        }
    }

    private fun startOvalAnimations(progressBar: ViewGroup) {
        if (isAnimating) {
            stopOvalAnimations(progressBar)
            //decreaseVolume()
        } else {
            isAnimating = true
            animateOvalsSequentially(progressBar)
            //increaseVolume()
        }
    }

    private fun stopOvalAnimations(progressBar: ViewGroup) {
        isAnimating = false
        currentOvalIndex = 0
        progressBar.removeAllViews()
    }

    private fun increaseVolume() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val newVolume = currentVolume + 1
        if (newVolume <= maxVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
        }
    }

    private fun decreaseVolume() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val newVolume = currentVolume - 1
        if (newVolume >= 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
        }
    }

    private fun increaseVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE + 1
                )
            )
        }
    }

    private fun decreaseVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100,
                    VibrationEffect.DEFAULT_AMPLITUDE - 1
                )
            )
        }
    }

    @SuppressLint("DiscouragedApi", "ObjectAnimatorBinding")
    private fun animateOvalsSequentially(progressBarLayout: ViewGroup) {
        if (currentOvalIndex > 12) {
            decreaseVolume()
            animateToWhite(progressBarLayout)
            return
        }

        val ovalId = resources.getIdentifier("oval$currentOvalIndex", "id", packageName)
        val ovalView = progressBarLayout.findViewById<View>(ovalId)
        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val startColor = ContextCompat.getColor(this, R.color.startColorGradient)

        val colorAnimator = ObjectAnimator.ofObject(
            ovalView.background, "color", ArgbEvaluator(), startColor, whiteColor
        )
        colorAnimator.duration = 200
        colorAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                currentOvalIndex++
                animateOvalsSequentially(progressBarLayout)
            }
        })

        colorAnimator.start()
    }



    private fun animateToWhite(progressBarLayout: ViewGroup) {
        val whiteColor = ContextCompat.getColor(this, R.color.white)

        for (i in currentOvalIndex until 12) {
            val ovalId = resources.getIdentifier("oval$i", "id", packageName)
            val ovalView = progressBarLayout.findViewById<View>(ovalId)
            val colorAnimator = createColorAnimator(ovalView, whiteColor)
            colorAnimator.duration = 200
            colorAnimator.start()
        }
    }

    private fun createColorAnimator(view: View, toColor: Int): ValueAnimator {
        val colorFrom = resources.getColor(R.color.startColorGradient)

        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, toColor)
        colorAnimator.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            val ovalDrawable = view.background as GradientDrawable
            ovalDrawable.setColor(color)
        }
        colorAnimator.duration = 200
        return colorAnimator
    }

    /*@SuppressLint("UseCompatLoadingForDrawables")
    private fun createOvalView(): View {
        val ovalView = View(this)
        ovalView.layoutParams = ViewGroup.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.oval_width),
            resources.getDimensionPixelSize(R.dimen.oval_height)
        )
        val ovalDrawable = resources.getDrawable(R.drawable.ellipse) as GradientDrawable
        ovalDrawable.setColor(resources.getColor(R.color.startColorGradient))
        ovalView.background = ovalDrawable
        return ovalView
    }*/
}