package com.pyramidal.luuck.ui.main.scene.games

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseGameFragment : Fragment() {
    private var balance: String? = null
    private var stake: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("balance", balance)
        outState.putString("stake", stake)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        balance = savedInstanceState.getString("balance")
        stake = savedInstanceState.getString("stake")
    }
}