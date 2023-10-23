package com.pyramidal.luuck.ui.main.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityMenuBinding
import com.pyramidal.luuck.databinding.SettingsActivityBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity
import com.pyramidal.luuck.ui.utils.HideUIConfigUtils

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { SettingsActivityBinding.inflate(layoutInflater) }
    private val bindingMenu by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private val audioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var previousVolume: Int = 0
    private val vibrator by lazy { getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    private var isSoundOn = false
    private var isVibrationOn = false
    private val MY_PERMISSIONS_REQUEST_AUDIO_SETTINGS = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        initializeBars()
        controlButton()
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
            // change block games
            bindingMenu.gameThree.setImageResource(R.drawable.item_small_block)
            bindingMenu.gameFour.setImageResource(R.drawable.item_small3_block)
        }
        binding.textResetScore.setOnClickListener {
            binding.textResetScore.startAnimation(animation)
            resetBalanceToDefault()
        }
    }

    private fun initializeBars() {
        setOvalColor(isSoundOn, binding.soundBar.progressBarLayout)
        setOvalColor(isVibrationOn, binding.vibrationBar.progressBarLayout)

        binding.soundBar.progressBarLayout.setOnClickListener {
            toggleSound()
        }

        binding.vibrationBar.progressBarLayout.setOnClickListener {
            toggleVibration()
        }
    }

    private fun toggleSound() {
        isSoundOn = !isSoundOn
        if (isSoundOn) {
            enableSystemSound()
        } else {
            disableSystemSound()
        }
        setOvalColor(isSoundOn, binding.soundBar.progressBarLayout)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_AUDIO_SETTINGS -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    toggleSound()
                } else {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Дозвіл на зміну звуку")
                    alertDialog.setMessage(
                        "Для використання цієї функції потрібно надати дозвіл на зміну звуку. " +
                                "Без цього дозволу функціональність може бути обмежено. " +
                                "Для надання дозволу перейдіть до налаштувань пристрою."
                    )
                    alertDialog.setPositiveButton("Перейти до налаштувань") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    alertDialog.setNegativeButton("Закрити") { _, _ ->
                        closeContextMenu()
                    }
                    alertDialog.show()
                }
                return
            }
        }
    }

    private fun toggleVibration() {
        isVibrationOn = !isVibrationOn
        if (isVibrationOn) {
            enableSystemVibration()
        } else {
            disableSystemVibration()
        }
        setOvalColor(isVibrationOn, binding.vibrationBar.progressBarLayout)
    }

    @SuppressLint("DiscouragedApi")
    private fun setOvalColor(isActive: Boolean, progressBar: ViewGroup) {
        val ovalColor = if (isActive) {
            resources.getColor(R.color.startColorGradient)
        } else {
            resources.getColor(R.color.white)
        }
        for (i in 1..12) {
            val ovalId = resources.getIdentifier("oval$i", "id", packageName)
            val ovalView = progressBar.findViewById<View>(ovalId)
            val ovalDrawable = ovalView.background as GradientDrawable
            ovalDrawable.setColor(ovalColor)
        }
    }

    private fun enableSystemSound() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0)
    }

    private fun disableSystemSound() {
        previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    }

    private fun enableSystemVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    1000,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(1000)
        }
    }

    private fun disableSystemVibration() {
        vibrator.cancel()
    }

    private fun resetBalanceToDefault() {
        sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("balance", getString(R.string.title_total))
        editor.apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val go = Intent(this@SettingsActivity, MenuActivity::class.java)
        startActivity(go)
        finish()
    }
}