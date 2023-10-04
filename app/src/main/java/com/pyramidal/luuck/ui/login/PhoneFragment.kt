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
            setFullPhone()
        }
        binding.btnPlay.setOnClickListener {
            binding.btnPlay.startAnimation(animation)
            val selectedCountryCode = binding.codeCountry.selectedCountryCode
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val fullPhoneNumber = selectedCountryCode + phoneNumber

            /*if (phoneNumber.isEmpty()) {
                Toast.makeText(
                    context,
                    "Phone number cannot be empty",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }else if (hasConsecutiveDigits(fullPhoneNumber)) {
                Toast.makeText(
                    context,
                    "Invalid phone: $fullPhoneNumber (consecutive digits not allowed)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else if (isPhoneValid(fullPhoneNumber)) {
                savePhoneToSharedPreferences(fullPhoneNumber)
                loadingNextActivity()
            } else {
                Toast.makeText(
                    context,
                    "Invalid phone: $fullPhoneNumber correct format: +380 ${context?.getString(R.string.phone_hint)}",
                    Toast.LENGTH_LONG
                ).show()
            }*/

            if (hasConsecutiveDigits(fullPhoneNumber)) {
                Toast.makeText(
                    context,
                    "Invalid phone: $fullPhoneNumber (consecutive digits not allowed)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else if (phoneNumber.isEmpty()) {
                Toast.makeText(
                    context,
                    "Phone number cannot be empty",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else if (isPhoneValid(fullPhoneNumber)) {
                savePhoneToSharedPreferences(fullPhoneNumber)
                loadingNextActivity()
            } else {
                Toast.makeText(
                    context,
                    "Invalid phone: $fullPhoneNumber correct format: +380 ${context?.getString(R.string.phone_hint)}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnBack.setOnClickListener {
            binding.btnBack.startAnimation(animation)
            fragmentManager?.popBackStack()
        }
    }

    private fun setFullPhone() {
        countryCode = binding.codeCountry.selectedCountryCode
        phoneNumber = binding.phoneNumber.text.toString().trim()
        fullPhoneNumber = countryCode + phoneNumber
    }

    private fun hasConsecutiveDigits(phoneNumber: String): Boolean {
        val consecutiveDigitsPattern = "(\\d)\\1{5,}"
        val regex = Regex(consecutiveDigitsPattern)
        return regex.containsMatchIn(phoneNumber)
    }

    private fun isPhoneValid(phone: String): Boolean {
        if (hasConsecutiveDigits(phone)) {
            return false
        }
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
        requireActivity().finish()
    }
}