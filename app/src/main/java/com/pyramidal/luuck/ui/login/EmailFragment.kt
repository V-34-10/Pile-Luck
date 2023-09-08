package com.pyramidal.luuck.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentEmailBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity

class EmailFragment : Fragment() {

    private lateinit var binding: FragmentEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        binding.textAnonymousMode.setOnClickListener {
            binding.textAnonymousMode.startAnimation(animation)
            loadingNextActivity()
        }
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(animation)
            val enteredEmail = binding.emailLayout.text.toString()
            if (isEmailValid(enteredEmail)) {
                saveEmailToSharedPreferences(enteredEmail)
            } else {
                Toast.makeText(
                    context,
                    "Invalid email: $enteredEmail correct format: ${context?.getString(R.string.testEmail)}",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            loadingNextActivity()


        }
        binding.btnBack.setOnClickListener {
            binding.btnBack.startAnimation(animation)
            fragmentManager?.popBackStack()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val regexPattern = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
        return regexPattern.matches(email)
    }

    private fun saveEmailToSharedPreferences(email: String) {
        val sharedPreferences = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("email_key", email)
        editor?.apply()
    }

    private fun loadingNextActivity() {
        // run MenuActivity
        val go = Intent(activity, MenuActivity::class.java)
        startActivity(go)
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
        requireActivity().finish()
    }
}