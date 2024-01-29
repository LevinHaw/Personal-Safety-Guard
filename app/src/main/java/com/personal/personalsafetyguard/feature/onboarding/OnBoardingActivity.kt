package com.personal.personalsafetyguard.feature.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.databinding.ActivityMainBinding
import com.personal.personalsafetyguard.databinding.OnboardingActivityBinding

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  binding = OnboardingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}
