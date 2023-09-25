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
import com.pyramidal.luuck.ui.main.scene.SlotAdapter
import com.pyramidal.luuck.ui.main.scene.model.SlotItem
import com.pyramidal.luuck.ui.main.settings.BalanceResetListener
import com.pyramidal.luuck.ui.utils.StakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.setStakeManager
import com.pyramidal.luuck.ui.utils.UpdateStakeUI.updateStakeUI


class GameFifeFragment : Fragment(), BalanceResetListener {
    private lateinit var binding: FragmentGameFifeBinding
    private lateinit var slotAdapter: SlotAdapter
    private lateinit var slotItems: List<SlotItem>
    private var slotList = mutableListOf(
        R.drawable.slot_001,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_002,
        R.drawable.slot_003,
        R.drawable.slot_005,
        R.drawable.slot_005,
        R.drawable.slot_005,
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

        }
    }

    private fun initSlotsRecycler() {
        slotItems = slotList.map { SlotItem(it) }
        slotAdapter = SlotAdapter(slotItems)
        binding.sceneGames?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = slotAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    override fun resetBalanceToDefault(newBalance: Int) {
        binding.textTotal?.text ?: "Total $newBalance"
    }
}