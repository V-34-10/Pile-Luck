package com.pyramidal.luuck.ui.main.scene.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentGameFourBinding
import com.pyramidal.luuck.databinding.FragmentGameThreeBinding

class GameFourFragment : Fragment() {
    private lateinit var binding: FragmentGameFourBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameFourBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}