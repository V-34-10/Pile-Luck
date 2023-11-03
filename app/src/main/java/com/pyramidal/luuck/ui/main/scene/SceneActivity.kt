package com.pyramidal.luuck.ui.main.scene

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivitySceneBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity
import com.pyramidal.luuck.ui.main.scene.games.GameFifeFragment
import com.pyramidal.luuck.ui.main.scene.games.GameFirstFragment
import com.pyramidal.luuck.ui.main.scene.games.GameFourFragment
import com.pyramidal.luuck.ui.main.scene.games.GameThreeFragment
import com.pyramidal.luuck.ui.utils.HideUIConfigUtils
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.resetWinToDefault

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        initFragmentGame()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            val go = Intent(this@SceneActivity, MenuActivity::class.java)
            startActivity(go)
            finish()
        }
    }

    private fun initFragmentGame() {
        val nameGame = intent.getStringExtra("name_game")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when (nameGame) {
            "RomeEgypt" -> {
                val fragmentA = GameFirstFragment()
                bundle.putString("name_game", "RomeEgypt")
                fragmentA.arguments = bundle
                fragmentTransaction.replace(R.id.fragment_container, fragmentA)
            }

            "HelioPOPolis" -> {
                val fragmentB = GameFirstFragment()
                bundle.putString("name_game", "HelioPOPolis")
                fragmentB.arguments = bundle
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "Cleo'sBook" -> {
                val fragmentB = GameThreeFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "FortunePlay" -> {
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                val fragmentB = GameFourFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "EgyptPlay" -> {
                val fragmentB = GameFifeFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            else -> {}
        }
        fragmentTransaction.commit()
    }
}