package com.pyramidal.luuck.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.pyramidal.luuck.R
import com.pyramidal.luuck.ui.main.privacy.RemotePrivacy
import com.pyramidal.luuck.databinding.FragmentSignUpBinding
import com.pyramidal.luuck.ui.main.privacy.PrivacyActivity

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controlButton()
    }

    private fun controlButton() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        binding.textPrivacy.setOnClickListener {
            binding.textPrivacy.startAnimation(animation)
            val go = Intent(activity, PrivacyActivity::class.java)
            startActivity(go)
        }
        binding.btnPhone.setOnClickListener {
            binding.btnPhone.startAnimation(animation)
            navigateToFragment(PhoneFragment::class.java)
        }
        binding.btnEmail.setOnClickListener {
            binding.btnEmail.startAnimation(animation)
            navigateToFragment(EmailFragment::class.java)
        }
    }

    private fun navigateToFragment(fragmentClass: Class<*>) {
        try {
            val fragment = fragmentClass.newInstance() as Fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}