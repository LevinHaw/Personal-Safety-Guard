package com.personal.personalsafetyguard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.personal.personalsafetyguard.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.top_text_anim)
        val middleAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_text_anim)
        val bottomAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.middle_text_anim)

        binding.topTV.startAnimation(topAnimation)
        binding.middleTV.startAnimation(middleAnimation)
        binding.bottomTV.startAnimation(bottomAnimation)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser: FirebaseUser? = auth.currentUser

        // Delay for the splash screen timeout
        val splashScreenTimeOut: Long = 3000

        Handler(Looper.myLooper() ?: return).postDelayed({
            if (currentUser != null) {
                // User is already authenticated, go to the main activity
                goToMainActivity()
            } else {
                // User is not logged in, move to the LoginPage
                goToLoginPage()
            }
        }, splashScreenTimeOut)
    }

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this@SplashScreen, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun goToLoginPage() {
        val loginPageIntent = Intent(this@SplashScreen, LoginPage::class.java)
        startActivity(loginPageIntent)
        finish()
    }
}
