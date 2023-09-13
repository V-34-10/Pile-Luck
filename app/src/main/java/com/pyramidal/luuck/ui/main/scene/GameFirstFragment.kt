package com.pyramidal.luuck.ui.main.scene

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFirstBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFirstBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSlotsRecycler()
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        binding.btnSpin.setOnClickListener {
            if (isAnimationInProgress) {
                return@setOnClickListener
            }

            /*slotDataList.shuffle()
            slotItems = slotDataList.map { SlotItem(it) }
            slotAdapter.updateData(slotItems)

            //slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())

            binding.sceneGame.postDelayed({
                slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
            }, 100)*/

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    isAnimationInProgress = true
                }

                override fun onAnimationEnd(animation: Animation?) {
                    isAnimationInProgress = false
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.btnSpin.startAnimation(animation)

            switchGame()
            slotAdapter.updateData(slotItems)

            if (slotItems.size == binding.sceneGame.layoutManager?.childCount) {
                slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
            } else {
                binding.sceneGame.post {
                    slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
                }
            }

        }
    }

    private fun switchGame() {
        val orientation = resources.configuration.orientation
        slotItems = slotSecondList.map { SlotItem(it) }
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
        switchGame()
        slotAdapter = SlotAdapter(slotItems)
        binding.sceneGame.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = slotAdapter
        }
    }
}