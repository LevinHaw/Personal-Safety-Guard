package com.personal.personalsafetyguard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.personal.personalsafetyguard.databinding.ActivitySignUpPageBinding
import com.personal.personalsafetyguard.feature.onboarding.OnBoardingActivity

class SignUpPage : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this, OnBoardingActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tolong di isi, tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRedirectLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginPage::class.java)
            startActivity(loginIntent)
        }

    }
}