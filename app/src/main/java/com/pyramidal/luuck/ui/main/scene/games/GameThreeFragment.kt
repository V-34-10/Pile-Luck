package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.extractNumberFromText
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.isBalanceSaved
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.saveNewBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI
import java.util.Random

class GameThreeFragment : Fragment(), BalanceResetListener {
    private lateinit var binding: FragmentGameThreeBinding
    private lateinit var stakeManager: StakeManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameThreeBinding.inflate(layoutInflater, container, false)

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
        var currentRotation = 0f
        binding.btnSpin.setOnClickListener {
            binding.btnSpin.startAnimation(animation)
            val minAngle = 0f
            val maxAngle = 720f
            val randomAngle = minAngle + Random().nextFloat() * (maxAngle - minAngle)
            val rotateAnimation = RotateAnimation(
                currentRotation,
                currentRotation + randomAngle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )

            rotateAnimation.duration = 2000
            rotateAnimation.fillAfter = true

            binding.wheel.startAnimation(rotateAnimation)

            rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                @SuppressLint("SetTextI18n")
                override fun onAnimationEnd(animation: Animation?) {
                    // Store the current rotation angle
                    currentRotation += randomAngle

                    val bidDigits = extractNumberFromText(binding.textBid.text.toString())
                    var sumWin = extractNumberFromText(binding.textWin.text.toString())
                    var totalSum = extractNumberFromText(binding.textTotal.text.toString())

                    if (calculateCoefficient(currentRotation).toInt() == 0) {
                        totalSum -= bidDigits
                        binding.textTotal.text = "Total $totalSum"
                    } else {
                        val newSumWin = bidDigits * calculateCoefficient(currentRotation)
                        totalSum += newSumWin.toInt()
                        binding.textTotal.text = "Total $totalSum"
                        sumWin += newSumWin.toInt()
                        binding.textWin.text = "WIN $sumWin"

                        //saveBalance
                        activity?.let { it1 ->
                            saveNewBalance(
                                it1,
                                "Total $totalSum",
                                bidDigits.toString()
                            )
                        }
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
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
    }

    private fun calculateCoefficient(degrees: Float): Float {
        return when (normalizeDegrees(degrees)) {  //normalizeDegrees [0, 360]
            in 5f..32f, in 180f..212f -> 1f // 1x
            in 0f..10f, in 240f..280f -> 2f // 2x
            in 35f..65f, in 213f..235f -> 1.3f // 1.3x
            in 105f..139f -> 1f // 1x
            in 140f..150f, in 310f..358f -> 2f // 2x
            in 140f..177f, in 281f..305f -> 0f // 0x
            in 70f..100f -> 2f // 2x
            else -> {
                return 1f
            }
        }
    }

    private fun normalizeDegrees(degrees: Float): Float {
        var normalizedDegrees = degrees % 360
        if (normalizedDegrees < 0) {
            normalizedDegrees += 360
        }
        return if (normalizedDegrees == 360f) 0f else normalizedDegrees
    }

    @SuppressLint("SetTextI18n")
    override fun resetBalanceToDefault(newBalance: Int) {
        binding.textTotal.text = "Total $newBalance"
    }
}