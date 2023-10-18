package com.pyramidal.luuck.ui.main.scene.games

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseGameFragment : Fragment() {
    /*private lateinit var Balance: String
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
    }*/

    var balance: String? = null
    var stake: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            balance = savedInstanceState.getString("balance")
            stake = savedInstanceState.getString("stake")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("balance", balance)
        outState.putString("stake", stake)
    }

    protected fun updateBalanceAndStake(balance: String, stake: String) {
        this.balance = balance
        this.stake = stake
    }
}