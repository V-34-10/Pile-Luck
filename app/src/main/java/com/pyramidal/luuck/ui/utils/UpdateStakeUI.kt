package com.pyramidal.luuck.ui.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFifeBinding
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

        if (binding is FragmentGameFirstBinding) {
            // Update UI with the current stake value
            binding.textBid.text = currentStake.toString()
            // Enable/disable buttons based on stake limits
            binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
            binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
        } else if (binding is FragmentGameThreeBinding) {
            binding.textBid.text = currentStake.toString()
            binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
            binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
        } else if (binding is FragmentGameFourBinding) {
            binding.textBid.text = currentStake.toString()
            binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
            binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
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

    fun saveNewBalance(context: Context, binding: ViewBinding) {
        sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val currentBalance: String = when (binding) {
            is FragmentGameFirstBinding -> binding.textTotal.text.toString()
            is FragmentGameThreeBinding -> binding.textTotal.text.toString()
            is FragmentGameFourBinding -> binding.textTotal.text.toString()
            is FragmentGameFifeBinding -> binding.textTotal?.text.toString()
            else -> context.getString(R.string.title_total)
        }
        editor.putString("balance", currentBalance)
        editor.apply()
    }

    fun updateBalance(context: Context, binding: ViewBinding) {
        sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)!!
        val currentBalance = sharedPref.getString("balance", context.getString(R.string.title_total))

        when (binding) {
            is FragmentGameFirstBinding -> binding.textTotal.text = currentBalance
            is FragmentGameThreeBinding -> binding.textTotal.text = currentBalance
            is FragmentGameFourBinding -> binding.textTotal.text = currentBalance
            is FragmentGameFifeBinding -> binding.textTotal?.text = currentBalance
        }
    }

    fun isBalanceSaved(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("my_prefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPref.contains("balance")
    }
}