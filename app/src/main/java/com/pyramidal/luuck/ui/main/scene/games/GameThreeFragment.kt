package com.pyramidal.luuck.ui.main.scene.games

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
import com.pyramidal.luuck.ui.utils.StakeManager
import java.util.Random

class GameThreeFragment : Fragment() {
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
        val minStakePercentage = 0.05 // 5%
        val maxStakePercentage = 0.10 // 10%
        val stakeStep = 50
        stakeManager = StakeManager(totalSum, minStakePercentage, maxStakePercentage, stakeStep)

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
                    Toast.makeText(requireContext(), coefficient.toString(), Toast.LENGTH_LONG)
                        .show()

                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }

        binding.btnMinus.setOnClickListener {
            binding.btnMinus.startAnimation(animation)
            stakeManager.decreaseStake()
            updateStakeUI()
        }
        binding.btnPlus.setOnClickListener {
            binding.btnPlus.startAnimation(animation)
            stakeManager.increaseStake()
            updateStakeUI()
        }
    }

    private fun updateStakeUI() {
        val currentStake = stakeManager.getCurrentStake()

        // Update UI with the current stake value
        binding.textBid.text = currentStake.toString()

        // Enable/disable buttons based on stake limits
        binding.btnMinus.isEnabled = stakeManager.canDecreaseStake()
        binding.btnPlus.isEnabled = stakeManager.canIncreaseStake()
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

        return when {
            (normalizedDegrees in 0f..30f) -> 2f  // 2x
            (normalizedDegrees in 40f..60f) -> 1.3f  // 1.3x
            (normalizedDegrees in 80f..90f) -> 1f  // 1x

            (normalizedDegrees in 95f..115f) -> 2f  // 2x
            (normalizedDegrees in 130f..150f) -> 0f  // 0x
            (normalizedDegrees in 160f..185f) -> 2f  // 2x

            (normalizedDegrees in 195f..225f) -> 1.3f  // 1.3x
            (normalizedDegrees in 240f..270f) -> 1f  // 1x
            (normalizedDegrees in 270f..290f) -> 0f  // 0x

            (normalizedDegrees in 295f..330f) -> 1f  // 1x
            (normalizedDegrees in 340f..360f) -> 2f  // 2x
            else -> -1f
        }
    }

    private fun normalizeDegrees(degrees: Float): Float {
        var normalizedDegrees = degrees % 360  // Відкидаємо повні обороти
        if (normalizedDegrees < 0) {
            normalizedDegrees += 360  // Нормалізуємо до діапазону [0, 360]
        }
        return normalizedDegrees
    }
}