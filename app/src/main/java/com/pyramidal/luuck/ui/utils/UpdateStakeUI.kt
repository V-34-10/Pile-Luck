package com.pyramidal.luuck.ui.utils

import androidx.viewbinding.ViewBinding
import com.pyramidal.luuck.databinding.FragmentGameFifeBinding
import com.pyramidal.luuck.databinding.FragmentGameFirstBinding
import com.pyramidal.luuck.databinding.FragmentGameFourBinding
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding

object UpdateStakeUI {
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
        val minStakePercentage = 0.05 // 5%
        val maxStakePercentage = 0.10 // 10%
        val stakeStep = 50
        return StakeManager(totalSum, minStakePercentage, maxStakePercentage, stakeStep)
    }

    fun extractNumberFromText(text: String): Int {
        val digitsOnly = text.replace(Regex("\\D"), "")
        return digitsOnly.toIntOrNull() ?: 0
    }

}