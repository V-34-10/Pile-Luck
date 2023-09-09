package com.pyramidal.luuck.ui.main.menu

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityMenuBinding
import com.pyramidal.luuck.ui.login.LoginActivity
import com.pyramidal.luuck.ui.main.scene.SceneActivity
import com.pyramidal.luuck.ui.main.settings.SettingsActivity
import com.pyramidal.luuck.utils.HideUIConfigUtils

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var sharedPref: SharedPreferences
    /*private var dataList = listOf(
        LargeData(R.drawable.item_large, "Rome&Egypt"),
        SmallData(R.drawable.item_large2, "HelioPOPolis"),
        SmallData(R.drawable.item_small_block, "Cleo'sBook"),
        LargeData(R.drawable.item_small3_block, "FortunePlay"),
        SmallData(R.drawable.item_large3, "EgyptPlay"),
        SmallData(R.drawable.item_small2, "SunGoddess"),
        LargeData(R.drawable.item_large4, "Mysterious")
    )*/
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
        //initRecycler()
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
                finish()
            }
        }
    }

    /*private fun initRecycler() {
        val spanCount = 2
        val layoutManager = GridLayoutManager(this, spanCount)



        val adapter = RecyclerAdapter(dataList)
        binding.gamesContainer.adapter = adapter
        binding.gamesContainer.layoutManager = layoutManager
        layoutManager.spanSizeLookup = SpanSizeLookup(adapter)

        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(item: Any) {
                val intent = Intent(this@MenuActivity, SceneActivity::class.java)
                intent.putExtra(
                    "nameGame", when (item) {
                        is LargeData -> item.nameGame
                        is SmallData -> item.nameGame
                        else -> ""
                    }
                )
                startActivity(intent)
                overridePendingTransition(R.anim.scale_up, R.anim.scale_up)
                finish()
            }
        })
    }*/

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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //no back click
    }
}