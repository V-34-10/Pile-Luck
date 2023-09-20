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
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI

class GameFourFragment : Fragment() {
    private lateinit var binding: FragmentGameFourBinding
    private lateinit var stakeManager: StakeManager
    private var totalSum: Int = 0
    private var totalSumStr: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFourBinding.inflate(layoutInflater, container, false)

        totalSumStr = getString(R.string.title_total)
        val totalSumDigitsOnly = totalSumStr.replace(Regex("\\D"), "")
        totalSum = totalSumDigitsOnly.toIntOrNull() ?: 0
        setStakeManager(totalSum)

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
    }

    @SuppressLint("SetTextI18n")
    private fun openChest(chestLayout: ChestLayoutBinding) {
        val openChestAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.open_chest_animation)

        openChestAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val possibleWinAmounts = listOf(0, 150, 300, 500)
                val winAmount = possibleWinAmounts.random()

                chestLayout.winAmountText.text = "+$winAmount"
                chestLayout.winAmountText.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        chestLayout.chestClosed.startAnimation(openChestAnimation)
    }
}