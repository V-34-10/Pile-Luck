package com.pyramidal.luuck.ui.main.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ActivityMenuBinding
import com.pyramidal.luuck.ui.main.scene.SceneActivity
import com.pyramidal.luuck.utils.HideUIConfigUtils

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        HideUIConfigUtils.hideUINavigation(this)
        initRecycler()
    }

    private fun controlButton() {

        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        binding.btnExit.setOnClickListener {
            binding.btnExit.startAnimation(animation)
            //loadingNextActivity()
        }

        binding.btnRegister.setOnClickListener {
            binding.btnRegister.startAnimation(animation)
            //loadingNextActivity()
        }
        binding.btnSettings.setOnClickListener {
            binding.btnSettings.startAnimation(animation)

        }
    }

    private fun initRecycler() {
        val spanCount = 2
        val layoutManager = GridLayoutManager(this, spanCount)
        val dataList = listOf(
            LargeData(R.drawable.item_large),
            SmallData(R.drawable.item_large2),
            SmallData(R.drawable.item_small),
            SmallData(R.drawable.item_small3),
            LargeData(R.drawable.item_large3),
            SmallData(R.drawable.item_small2),
            LargeData(R.drawable.item_large4)
        )

        val adapter = RecyclerAdapter(dataList, object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(item: Any) {
                when (item) {
                    is SmallData -> {
                        val intent = Intent(this@MenuActivity, SceneActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.scale_up, R.anim.scale_up)
                        finish()
                    }

                    is LargeData -> {
                        val intent = Intent(this@MenuActivity, SceneActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.scale_up, R.anim.scale_up)
                        finish()
                    }
                }
            }
        })
        binding.gamesContainer.adapter = adapter
        binding.gamesContainer.layoutManager = layoutManager
        layoutManager.spanSizeLookup = SpanSizeLookup(adapter)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //no back click
    }
}