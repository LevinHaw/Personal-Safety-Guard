package com.personal.personalsafetyguard

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.personal.personalsafetyguard.databinding.ActivityLoginPageBinding
import com.personal.personalsafetyguard.feature.onboarding.OnBoardingActivity

class LoginPage : AppCompatActivity() {

        private lateinit var binding: ActivityLoginPageBinding
        private lateinit var firebaseAuth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
            supportActionBar?.hide()

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        val intent = Intent(this, OnBoardingActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kolom tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }


        binding.tvRedirectSignUp.setOnClickListener {
            val signupIntent = Intent(this, SignUpPage::class.java)
            startActivity(signupIntent)
        }
            binding.tvForgotPassword.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.box_forgot_pass, null)
                val userEmail = view.findViewById<EditText>(R.id.editBox)
                builder.setView(view)
                val dialog = builder.create()
                view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                    compareEmail(userEmail)
                    dialog.dismiss()
                }
                view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                    dialog.dismiss()
                }
                if (dialog.window != null){
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()
            }
            binding.tvRedirectSignUp.setOnClickListener {
                val signupIntent = Intent(this, SignUpPage::class.java)
                startActivity(signupIntent)
            }
        }
    //Outside onCreate
    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}