package com.pyramidal.luuck.ui.main.scene.games

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFifeBinding
import com.pyramidal.luuck.ui.main.scene.SlotItemClickListener
import com.pyramidal.luuck.ui.main.scene.SlotMinerAdapter
import com.pyramidal.luuck.ui.main.scene.model.SlotItem
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.UpdateStakeUI
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.extractNumberFromText
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.isBalanceSaved
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.saveNewBalance


class GameFifeFragment : BalanceResetListener, SlotItemClickListener, BaseGameFragment() {
    private lateinit var binding: FragmentGameFifeBinding
    private lateinit var slotAdapter: SlotMinerAdapter
    private lateinit var slotItems: List<SlotItem>
    private var slotListDefault = mutableListOf(
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005
    )
    private var slotListCorrect = setOf(
        R.drawable.slot_001,
        R.drawable.slot_002,
        R.drawable.slot_003,
        R.drawable.slot_004
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFifeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initSlotsRecycler()
            controlButton()
            slotAdapter.setItemClickListener(this)
            //updateBalance
            activity?.let { context ->
                if (isBalanceSaved(context)) {
                    UpdateStakeUI.updateBalance(context, binding)
                }
            }
            if (savedInstanceState != null) {
                val balance = binding.textTotal?.text.toString()
                val stake = binding.textBid?.text.toString()
                updateBalanceAndStake(balance, stake)
            }
        }
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        val buttonBidPairs = listOf(
            binding.buttonBidFirst to 50,
            binding.buttonBidSecond to 100,
            binding.buttonBidThree to 150,
            binding.buttonBidFour to 200,
            binding.buttonBidFife to 500,
            binding.buttonBidSix to 750
        )

        buttonBidPairs.forEach { (button, bidValue) ->
            button?.setOnClickListener {
                button.startAnimation(animation)
                binding.textBid?.text = bidValue.toString()
            }
        }
        binding.btnSpin?.setOnClickListener {
            binding.btnSpin?.startAnimation(animation)
            slotItems = slotListDefault.map { SlotItem(it) }
            binding.sceneGames?.let { slotAdapter.updateData(slotItems) }
        }
    }

    private fun initSlotsRecycler() {
        slotItems = slotListDefault.map { SlotItem(it) }
        slotAdapter = SlotMinerAdapter(slotItems)
        binding.sceneGames?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = slotAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun resetBalanceToDefault(newBalance: Int) {
        binding.textTotal?.text = "Total $newBalance"
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClick(position: Int, slotItem: SlotItem) {
        if (isSlotItemOfCorrectType(slotItem)) {
            updateBalance()
            //saveBalance
            activity?.let { it1 -> saveNewBalance(it1, binding) }
        } else {
            val currentBid = extractNumberFromText(binding.textBid?.text.toString())
            var currentBalance = extractNumberFromText(binding.textTotal?.text.toString())
            currentBalance -= currentBid
            binding.textTotal?.text = "Total $currentBalance"
        }
    }

    private fun isSlotItemOfCorrectType(slotItem: SlotItem): Boolean {
        return slotListCorrect.contains(slotItem.imageResId)
    }

    @SuppressLint("SetTextI18n")
    private fun updateBalance() {
        val currentBid = extractNumberFromText(binding.textBid?.text.toString())
        var currentBalance = extractNumberFromText(binding.textTotal?.text.toString())
        var currentWin = extractNumberFromText(binding.textWin?.text.toString())
        currentBalance += (currentBid * 2)
        binding.textTotal?.text = "Total $currentBalance"
        currentWin += (currentBid * 2)
        binding.textWin?.text = "WIN $currentWin"
    }
}