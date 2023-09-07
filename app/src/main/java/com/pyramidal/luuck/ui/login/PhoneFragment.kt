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
import com.pyramidal.luuck.databinding.FragmentPhoneBinding
import com.pyramidal.luuck.ui.main.menu.MenuActivity

class PhoneFragment : Fragment() {

    private lateinit var binding: FragmentPhoneBinding
    private lateinit var countryCode: String
    private var fullPhoneNumber: String = ""
    private lateinit var phoneNumber: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(layoutInflater, container, false)
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

        binding.codeCountry.setOnCountryChangeListener {
            countryCode = binding.codeCountry.selectedCountryCode
            phoneNumber = binding.phoneNumber.text.toString()
            fullPhoneNumber = countryCode + phoneNumber
        }
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(animation)
            if (isPhoneValid(fullPhoneNumber)) {
                savePhoneToSharedPreferences(fullPhoneNumber)
            } else {
                Toast.makeText(
                    context,
                    "Invalid phone: $fullPhoneNumber, correct format: +380 ${context?.getString(R.string.phone_hint)}",
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

    private fun isPhoneValid(phone: String): Boolean {
        val pattern = Patterns.PHONE// check phone
        return pattern.matcher(phone).matches()
    }

    private fun savePhoneToSharedPreferences(phone: String) {
        val sharedPreferences = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("phone_key", phone)
        editor?.apply()
    }

    private fun loadingNextActivity() {
        // run MenuActivity
        val go = Intent(activity, MenuActivity::class.java)
        startActivity(go)
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }
}