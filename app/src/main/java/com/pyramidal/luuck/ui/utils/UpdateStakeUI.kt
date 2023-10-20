package com.pyramidal.luuck.ui.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFirstBinding
import com.pyramidal.luuck.databinding.FragmentGameFourBinding
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding

object UpdateStakeUI {
    private lateinit var sharedPref: SharedPreferences
    fun updateStakeUI(
        binding: ViewBinding,
        stakeManager: StakeManager
    ) {
        val currentStake = stakeManager.getCurrentStake()

        when (binding) {
            is FragmentGameFirstBinding -> {
                binding.textBid.text = currentStake.toString()
                binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
                binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
            }

            is FragmentGameThreeBinding -> {
                binding.textBid.text = currentStake.toString()
                binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
                binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
            }

            is FragmentGameFourBinding -> {
                binding.textBid.text = currentStake.toString()
                binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
                binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
            }
        }
    }

    fun setStakeManager(totalSum: Int): StakeManager {
        val minStakePercentage = 0.01 // 1%
        val maxStakePercentage = 1.0 // 100%
        val stakeStep = 50
        return StakeManager(totalSum, minStakePercentage, maxStakePercentage, stakeStep)
    }

    fun extractNumberFromText(text: String): Int {
        val digitsOnly = text.replace(Regex("\\D"), "")
        return digitsOnly.toIntOrNull() ?: 0
    }

    fun saveNewBalance(context: Context, balance: String, stake: String?) {
        sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("balance", balance)
        editor.putString("stake", stake)
        editor.apply()
    }

    fun updateBalance(context: Context): Pair<String?, String?> {
        sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)!!
        val balance = sharedPref.getString("balance", context.getString(R.string.title_total))
        val stake = sharedPref.getString("stake", context.getString(R.string.title_bid))
        return Pair(balance, stake)
    }

    fun isBalanceSaved(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPref.contains("balance")
    }
}