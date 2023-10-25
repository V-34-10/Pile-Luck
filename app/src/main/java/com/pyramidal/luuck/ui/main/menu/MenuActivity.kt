package com.pyramidal.luuck.ui.main.menu

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityMenuBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.ui.main.scene.SceneActivity
import com.pyramidal.luuck.ui.main.settings.SettingsActivity
import com.pyramidal.luuck.ui.utils.HideUIConfigUtils

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    private var dataGameNameList = listOf(
        "RomeEgypt",
        "HelioPOPolis",
        "Cleo'sBook",
        "FortunePlay",
        "EgyptPlay"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        checkUserSignIn()
        controlGameButton()
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnExit.setOnClickListener {
            binding.btnExit.startAnimation(animation)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            binding.btnRegister.startAnimation(animation)
            // run LoginActivity
            val go = Intent(this@MenuActivity, LoginActivity::class.java)
            startActivity(go)
            finish()
        }
        binding.btnSettings.setOnClickListener {
            binding.btnSettings.startAnimation(animation)
            // run LoginActivity
            val go = Intent(this@MenuActivity, SettingsActivity::class.java)
            startActivity(go)
            finish()
        }
    }

    private fun controlGameButton() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)

        val buttonList = listOf(
            binding.gameFirst,
            binding.gameSecond,
            binding.gameThree,
            binding.gameFour,
            binding.gameFife
        )

        for ((index, button) in buttonList.withIndex()) {
            button.setOnClickListener {
                it.startAnimation(animation)
                val go = Intent(this@MenuActivity, SceneActivity::class.java)
                go.putExtra("name_game", dataGameNameList[index])
                startActivity(go)
                finish()
            }
        }
    }

    private fun checkUserSignIn() {
        sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val hasEmail = sharedPref?.contains("email_key") ?: false
        val hasPhone = sharedPref?.contains("phone_key") ?: false
        if (hasEmail || hasPhone) {
            // change image on statusLevel
            binding.btnRegister.setImageResource(R.drawable.status_level)
            binding.btnRegister.isClickable = false
            binding.btnRegister.isFocusable = false
            binding.btnRegister.isEnabled = false
            // change block games
            binding.gameThree.setImageResource(R.drawable.item_small)
            binding.gameThree.isClickable = false
            binding.gameThree.isFocusable = false
            binding.gameFour.setImageResource(R.drawable.item_small3)
            binding.gameFour.isClickable = false
            binding.gameFour.isFocusable = false
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    override fun onBackPressed() {
        finish()
    }
}