package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ChestLayoutBinding
import com.pyramidal.luuck.databinding.FragmentGameFourBinding
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI

class GameFourFragment : Fragment(), BalanceResetListener {
    private lateinit var binding: FragmentGameFourBinding
    private lateinit var stakeManager: StakeManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFourBinding.inflate(layoutInflater, container, false)

        val totalSum = extractNumberFromText(binding.textTotal.text.toString())
        stakeManager = setStakeManager(totalSum)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        binding.btnMinus.setOnClickListener {
            binding.btnMinus.startAnimation(animation)
            stakeManager.decreaseStake()
            updateStakeUI(binding, stakeManager)
        }
        binding.btnPlus.setOnClickListener {
            binding.btnPlus.startAnimation(animation)
            stakeManager.increaseStake()
            updateStakeUI(binding, stakeManager)
        }
        binding.includeChestFirst.chestClosed.setOnClickListener {
            openChest(binding.includeChestFirst)
        }
        binding.includeChestSecond.chestClosed.setOnClickListener {
            openChest(binding.includeChestSecond)
        }
        binding.includeChestThree.chestClosed.setOnClickListener {
            openChest(binding.includeChestThree)
        }
        binding.includeChestFour.chestClosed.setOnClickListener {
            openChest(binding.includeChestFour)
        }
        val chestPairs = listOf(
            binding.includeChestFirst,
            binding.includeChestSecond,
            binding.includeChestThree,
            binding.includeChestFour
        )
        chestPairs.forEach { chestLayout ->
            chestLayout.chestClosed.visibility = View.VISIBLE
            chestLayout.chestOpen.visibility = View.GONE
            chestLayout.winAmountText.visibility = View.GONE
        }
        binding.btnSpin.setOnClickListener {
            binding.btnSpin.startAnimation(animation)
            chestPairs.forEach { chestLayout ->
                chestLayout.chestClosed.visibility = View.GONE
                chestLayout.chestOpen.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openChest(chestLayout: ChestLayoutBinding) {
        val openChestAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.open_chest_animation)

        openChestAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val possibleWinAmounts = listOf(0, 0.5, 1, 2)
                val winAmount = possibleWinAmounts.random().toInt()

                chestLayout.winAmountText.text = "x$winAmount"
                chestLayout.winAmountText.visibility = View.VISIBLE

                //add to balance
                val currentBid = extractNumberFromText(binding.textBid.text.toString())
                val currentBalance = extractNumberFromText(binding.textTotal.text.toString())
                val newBalance = currentBalance + (currentBid * winAmount)
                binding.textTotal.text = "Total $newBalance"

                //add to win
                val currentWin = extractNumberFromText(binding.textWin.text.toString())
                val newWin = currentWin + (currentBid * winAmount)
                binding.textTotal.text = "WIN $newWin"
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        chestLayout.chestClosed.startAnimation(openChestAnimation)
    }

    private fun extractNumberFromText(text: String): Int {
        val digitsOnly = text.replace(Regex("\\D"), "")
        return digitsOnly.toIntOrNull() ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun resetBalanceToDefault(newBalance: Int) {
        binding.textTotal.text = "Total $newBalance"
    }
}