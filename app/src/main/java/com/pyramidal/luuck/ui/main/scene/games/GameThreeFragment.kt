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
import android.widget.Toast
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI
import java.util.Random

class GameThreeFragment : Fragment(), BalanceResetListener {
    private lateinit var binding: FragmentGameThreeBinding
    private lateinit var stakeManager: StakeManager
    private var totalSum: Int = 0
    private var totalSumStr: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameThreeBinding.inflate(layoutInflater, container, false)

        totalSumStr = getString(R.string.title_total)
        val totalSumDigitsOnly = totalSumStr.replace(Regex("\\D"), "")
        totalSum = totalSumDigitsOnly.toIntOrNull() ?: 0
        /*val minStakePercentage = 0.05 // 5%
        val maxStakePercentage = 0.10 // 10%
        val stakeStep = 50
        stakeManager = StakeManager(totalSum, minStakePercentage, maxStakePercentage, stakeStep)*/
        stakeManager = setStakeManager(totalSum)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTextBid()
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
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Store the current rotation angle
                    currentRotation += randomAngle
                    val degrees = binding.arrow.rotation
                    val coefficient = calculateCoefficient(degrees)
                    /*Toast.makeText(requireContext(), coefficient.toString(), Toast.LENGTH_LONG)
                        .show()*/

                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
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

    private fun setTextBid() {
        val totalSumDigitsOnly = totalSumStr.replace(Regex("\\D"), "")
        val totalSum = totalSumDigitsOnly.toIntOrNull() ?: 0
        val fivePercent = (totalSum * 0.05).toInt()
        binding.textBid.text = fivePercent.toString()
    }

    private fun calculateCoefficient(degrees: Float): Float {
        val normalizedDegrees =
            normalizeDegrees(degrees)  //normalizeDegrees [0, 360]

        Toast.makeText(
            requireContext(),
            "Normalized Degrees: $normalizedDegrees",
            Toast.LENGTH_SHORT
        )
            .show()
        return when (normalizedDegrees) {
            in 0f..10f, in 175f..202f -> 2f // 2x
            in 15f..48f, in 205f..235f -> 1.3f // 1.3x
            in 50f..90f, in 240f..270f -> 1f // 1x
            in 95f..130f, in 337f..360f -> 2f // 2x
            in 130f..150f, in 270f..290f -> 0f // 0x
            else -> {
                Toast.makeText(
                    requireContext(),
                    "Invalid normalizedDegrees: $normalizedDegrees",
                    Toast.LENGTH_SHORT
                )
                    .show()
                -1f
            }
        }
    }

    private fun normalizeDegrees(degrees: Float): Float {
        var normalizedDegrees = degrees % 360
        if (normalizedDegrees < 0) {
            normalizedDegrees += 360
        }
        return normalizedDegrees
    }

    @SuppressLint("SetTextI18n")
    override fun resetBalanceToDefault(newBalance: Int) {
        binding.textTotal.text = "Total $newBalance"
    }
}