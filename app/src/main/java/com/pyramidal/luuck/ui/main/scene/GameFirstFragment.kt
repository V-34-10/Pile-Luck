package com.pyramidal.luuck.ui.main.scene

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
    private var slotDataList = mutableListOf(
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
    private lateinit var slotItems: List<SlotItem>
    private lateinit var slotAdapter: SlotAdapter
    private var isAnimationInProgress = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            slotDataList.shuffle()
            slotItems = slotDataList.map { SlotItem(it) }

            slotAdapter.updateData(slotItems)
            // Перевірка на співпадіння кількості елементів і видимих елементів у RecyclerView
            if (slotItems.size == binding.sceneGame.layoutManager?.childCount) {
                slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
            } else {
                // Якщо кількість не співпадає, виконати оновлення після вигляду анімації
                binding.sceneGame.post {
                    slotAdapter.playSpinAnimation(binding.sceneGame, requireContext())
                }
            }

        }
    }

    private fun initSlotsRecycler() {
        slotItems = slotDataList.map { SlotItem(it) }
        slotAdapter = SlotAdapter(slotItems)
        binding.sceneGame.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = slotAdapter
        }
    }
}