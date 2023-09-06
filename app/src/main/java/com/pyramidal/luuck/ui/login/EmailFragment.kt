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
import com.pyramidal.luuck.R
import com.pyramidal.luuck.databinding.FragmentEmailBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity
import com.pyramidal.luuck.ui.main.privacy.RemotePrivacy

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
            var enteredEmail = binding.emailLayout.text.toString()
            if (isEmailValid(enteredEmail)) {
                saveEmailToSharedPreferences(enteredEmail)
            } else {
                R.string.testEmail.toString().also { enteredEmail = it }
                saveEmailToSharedPreferences(enteredEmail)
            }
            loadingNextActivity()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS // check email
        return pattern.matcher(email).matches()
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
    }
}