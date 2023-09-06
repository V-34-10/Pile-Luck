package com.pyramidal.luuck.ui.main.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pyramidal.luuck.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //no back click
    }
}