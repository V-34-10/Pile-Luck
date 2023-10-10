package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.extractNumberFromText
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
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
                    val lastSumWin = extractNumberFromText(binding.textWin.text.toString())
                    val totalSum = extractNumberFromText(binding.textTotal.text.toString())

                    if (calculateCoefficient(currentRotation).toInt() == 0) {
                        val updatedTotalSum = totalSum - bidDigits
                        binding.textTotal.text = "Total $updatedTotalSum"
                    } else {
                        val newSumWin = bidDigits * calculateCoefficient(currentRotation)
                        val updatedTotalSum = totalSum + newSumWin
                        binding.textTotal.text = "Total $updatedTotalSum"
                        val updatedSumWin = lastSumWin + newSumWin
                        binding.textWin.text = "WIN $updatedSumWin"
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