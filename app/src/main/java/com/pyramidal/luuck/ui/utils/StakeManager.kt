package com.pyramidal.luuck.ui.utils

class StakeManager(
    private val totalSum: Int,
    private val minStakePercentage: Double,
    private val maxStakePercentage: Double,
    private val stakeStep: Int
) {
    private var currentStake = 0
    fun getCurrentStake(): Int {
        return currentStake
    }

    fun canIncreaseStake(): Boolean {
        val maxStake = (totalSum * maxStakePercentage).toInt()
        return currentStake < maxStake
    }

    fun increaseStake() {
        if (canIncreaseStake()) {
            currentStake += stakeStep
        }
    }

    fun canDecreaseStake(): Boolean {
        val minStake = (totalSum * minStakePercentage).toInt()
        return currentStake > minStake
    }

    fun decreaseStake() {
        if (canDecreaseStake()) {
            currentStake -= stakeStep
        }
    }
}