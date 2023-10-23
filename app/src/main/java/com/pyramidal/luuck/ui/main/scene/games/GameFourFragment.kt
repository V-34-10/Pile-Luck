package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.ChestLayoutBinding
import com.pyramidal.luuck.databinding.FragmentGameFourBinding
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.extractNumberFromText
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.isBalanceSaved
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.saveNewBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI

class GameFourFragment : Fragment() {
    private lateinit var binding: FragmentGameFourBinding
    private lateinit var stakeManager: StakeManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFourBinding.inflate(layoutInflater, container, false)
        view?.rotation = 0f
        val totalSum = extractNumberFromText(binding.textTotal.text.toString())
        stakeManager = setStakeManager(totalSum)
        updateStakeUI(binding, stakeManager)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controlButton()
        //updateBalance
        activity?.let { context ->
            if (isBalanceSaved(context)) {
                //updateBalance(context, binding)
                val (restoredBalance) = updateBalance(context)
                binding.textTotal.text = restoredBalance.toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("balance", binding.textTotal.text.toString())
        outState.putString("stake", binding.textBid.text.toString())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            binding.textTotal.text =
                savedInstanceState.getString(
                    "balance",
                    activity?.getString(R.string.title_total) ?: "Total 10000"
                )
        }
        if (savedInstanceState != null) {
            binding.textBid.text =
                savedInstanceState.getString(
                    "stake",
                    activity?.getString(R.string.title_bid) ?: "100"
                )
        }
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
        binding.btnSpin.setOnClickListener {
            binding.btnSpin.startAnimation(animation)
            chestPairs.forEach { chestLayout ->
                chestLayout.chestClosed.setImageResource(R.drawable.chest_close)
                chestLayout.winAmountText.visibility = View.GONE
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
                chestLayout.chestClosed.setImageResource(R.drawable.chest_open)

                //add to balance
                val currentBid = extractNumberFromText(binding.textBid.text.toString())
                val currentBalance = extractNumberFromText(binding.textTotal.text.toString())
                if (winAmount == 0) {
                    val newBalance = currentBalance - currentBid
                    binding.textTotal.text = "Total $newBalance"
                } else {
                    val newBalance = currentBalance + (currentBid * winAmount)
                    binding.textTotal.text = "Total $newBalance"
                }

                //add to win
                val currentWin = extractNumberFromText(binding.textWin.text.toString())
                val newWin = currentWin + (currentBid * winAmount)
                binding.textWin.text = "WIN $newWin"

                //saveBalance
                activity?.let { it1 ->
                    saveNewBalance(
                        it1,
                        binding.textTotal.text.toString(),
                        binding.textBid.text.toString()
                    )
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        chestLayout.chestClosed.startAnimation(openChestAnimation)
    }
}