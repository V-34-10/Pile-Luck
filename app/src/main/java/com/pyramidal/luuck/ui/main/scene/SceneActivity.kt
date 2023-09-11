package com.pyramidal.luuck.ui.main.scene

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityMenuBinding
import com.pyramidal.luuck.databinding.ActivitySceneBinding
import com.pyramidal.luuck.utils.HideUIConfigUtils

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        initFragmentGame()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //no back click
    }

    private fun initFragmentGame() {
        val nameGame = intent.getStringExtra("name_game")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when (nameGame) {
            "RomeEgypt" -> {
                val fragmentA = GameFirstFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentA)
            }

            "HelioPOPolis" -> {
                val fragmentB = GameSecondFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "Cleo'sBook" -> {
                val fragmentB = GameSecondFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "FortunePlay" -> {
                val fragmentB = GameSecondFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            "EgyptPlay" -> {
                val fragmentB = GameSecondFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragmentB)
            }

            else -> {}
        }
        fragmentTransaction.commit()
    }
}