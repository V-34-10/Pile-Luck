package com.pyramidal.luuck.ui.main.scene.games

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseGameFragment : Fragment() {
    private lateinit var Balance: String
    private lateinit var Stake: String

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("balance", Balance)
        outState.putString("stake", Stake)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            Balance = savedInstanceState.getString("balance").toString()
            Stake = savedInstanceState.getString("stake").toString()
        }
    }

    protected fun updateBalanceAndStake(balance: String, stake: String) {
        Balance = balance
        Stake = stake
    }
}