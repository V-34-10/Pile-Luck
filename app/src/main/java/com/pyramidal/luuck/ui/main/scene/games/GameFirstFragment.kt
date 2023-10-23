package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFirstBinding
import com.pyramidal.luuck.ui.main.scene.SlotAdapter
import com.pyramidal.luuck.ui.main.scene.model.SlotItem
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.extractNumberFromText
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.isBalanceSaved
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.saveNewBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateBalance
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI

class GameFirstFragment : Fragment() {
    private lateinit var binding: FragmentGameFirstBinding
    private var slotFirstList = mutableListOf(
        R.drawable.slot_1,
        R.drawable.slot_2,
        R.drawable.slot_3,
        R.drawable.slot_4,
        R.drawable.slot_5,
        R.drawable.slot_6,
        R.drawable.slot_1,
        R.drawable.slot_2,
        R.drawable.slot_3
    )
    private var slotSecondList = mutableListOf(
        R.drawable.slot_01,
        R.drawable.slot_02,
        R.drawable.slot_03,
        R.drawable.slot_04,
        R.drawable.slot_05,
        R.drawable.slot_01,
        R.drawable.slot_02,
        R.drawable.slot_03,
        R.drawable.slot_04
    )
    private lateinit var slotItems: List<SlotItem>
    private lateinit var slotAdapter: SlotAdapter
    private var isAnimationInProgress = false
    private lateinit var stakeManager: StakeManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFirstBinding.inflate(layoutInflater, container, false)

        val totalSum = extractNumberFromText(binding.textTotal.text.toString())
        stakeManager = setStakeManager(totalSum)
        updateStakeUI(binding, stakeManager)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSlotsRecycler()
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

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val (restoredBalance, restoredStake) = updateBalance(requireContext())
        binding.textTotal.text = restoredBalance.toString()
        binding.textBid.text = restoredStake.toString()
    }*/

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        binding.btnSpin.setOnClickListener {
            binding.btnSpin.startAnimation(animation)
            if (isAnimationInProgress) {
                return@setOnClickListener
            }

            stopAnimations()
            slotAdapter.updateData(emptyList())

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    isAnimationInProgress = true
                }

                @SuppressLint("SetTextI18n")
                override fun onAnimationEnd(animation: Animation?) {
                    isAnimationInProgress = false

                    val bidDigits = extractNumberFromText(binding.textBid.text.toString())
                    var sumWin = extractNumberFromText(binding.textWin.text.toString())
                    var totalSum = extractNumberFromText(binding.textTotal.text.toString())

                    if (calculateWinCoefficient(slotItems).toInt() == 0) {
                        totalSum -= bidDigits
                        binding.textTotal.text = "Total $totalSum"
                    } else {
                        val newSumWin = bidDigits * calculateWinCoefficient(slotItems)
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

            randomListGame()
            slotAdapter.updateData(slotItems)
            slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
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

    private fun calculateWinCoefficient(slotItems: List<SlotItem>): Float {
        val centralRow = slotItems.subList(3, 6)
        val uniqueItems = centralRow.distinct()
        return when (uniqueItems.size) {
            1 -> 5.0f
            2 -> 1.5f
            else -> 0.0f
        }
    }

    private fun stopAnimations() {
        val itemCount = slotAdapter.itemCount
        for (i in 0 until itemCount) {
            val holder =
                binding.sceneGame.findViewHolderForAdapterPosition(i) as? SlotAdapter.SlotViewHolder
            holder?.itemView?.clearAnimation()
        }
    }

    private fun randomListGame() {
        when (arguments?.getString("name_game")) {
            "RomeEgypt" -> {
                slotFirstList.shuffle()
                slotItems = slotFirstList.map { SlotItem(it) }
            }

            "HelioPOPolis" -> {
                slotSecondList.shuffle()
                slotItems = slotSecondList.map { SlotItem(it) }
            }
        }
    }

    private fun switchGame() {
        val orientation = resources.configuration.orientation
        when (arguments?.getString("name_game")) {
            "RomeEgypt" -> {
                slotFirstList.shuffle()
                slotItems = slotFirstList.map { SlotItem(it) }
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.backgroundFirstSecondGame.setBackgroundResource(R.drawable.background_first_game_land)
                } else {
                    binding.backgroundFirstSecondGame.setBackgroundResource(R.drawable.background_first_game)
                }
            }

            "HelioPOPolis" -> {
                slotSecondList.shuffle()
                slotItems = slotSecondList.map { SlotItem(it) }
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.backgroundFirstSecondGame.setBackgroundResource(R.drawable.background_second_game_land)
                } else {
                    binding.backgroundFirstSecondGame.setBackgroundResource(R.drawable.background_second_game)
                }
            }
        }
    }

    private fun initSlotsRecycler() {
        slotItems = slotSecondList.map { SlotItem(it) }
        switchGame()
        slotAdapter = SlotAdapter(slotItems)
        binding.sceneGame.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = slotAdapter
        }
    }
}