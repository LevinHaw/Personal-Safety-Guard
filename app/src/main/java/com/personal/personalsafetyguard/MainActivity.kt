package com.personal.personalsafetyguard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.cardview.widget.CardView
import com.personal.personalsafetyguard.ui.activities.HomeData
import com.personal.personalsafetyguard.ui.activities.Register
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.personal.personalsafetyguard.learnPage.LearnPage
import dev.skomlach.biometric.compat.*


class MainActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menu1: CardView = findViewById(R.id.vaultCard)
        val menu2: CardView = findViewById(R.id.finderCard)
        val menu3: CardView = findViewById(R.id.safeCard)
        val menu4: CardView = findViewById(R.id.learnCard)
        btnLogout = findViewById(R.id.btn_logOut)

        // Inisialisasi Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        menu1.setOnClickListener {
            val androidVersion = Build.VERSION.SDK_INT
            if (androidVersion <= 29) {
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
            } else if (androidVersion > 29) {
                if (isFaceBiometricSupported()) {
                    showBiometricPrompt(BiometricType.BIOMETRIC_FACE)
                } else {
                    showMessage("Biometrik wajah tidak terdaftar pada perangkat ini")
                }
            }
        }

        menu2.setOnClickListener {
            val intent = Intent(this, Proteksi::class.java)
            startActivity(intent)
        }

        menu3.setOnClickListener {
            val intent = Intent(this, FallTracker::class.java)
            startActivity(intent)
        }

        menu4.setOnClickListener {
            val intent = Intent(this, LearnPage::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Panggil fungsi logOut() ketika tombol diklik
            logOut()
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }


    }

    private fun showBiometricPrompt(biometricType: BiometricType) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autentikasi biometrik")
            .setSubtitle("Login menggunakan biometrik ${biometricType.name.toLowerCase()} ")
            .setNegativeButtonText("Batal")
            .build()

        val biometricPrompt = BiometricPrompt(
            this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                    showMessage("Autentikasi error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    showMessage("Autentikasi sukses dengan ${biometricType.name.toLowerCase()}!")
                    val intent = Intent(this@MainActivity, HomeData::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                    showMessage("Autentikasi gagal dengan ${biometricType.name.toLowerCase()}.")
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isFaceBiometricSupported(): Boolean {
        val faceId = BiometricAuthRequest(
            BiometricApi.LEGACY_API,
            BiometricType.BIOMETRIC_FACE
        )

        return BiometricManagerCompat.isHardwareDetected(faceId) &&
                BiometricManagerCompat.hasEnrolled(faceId) &&
                BiometricManagerCompat.isBiometricAvailable(faceId) &&
                BiometricManagerCompat.isSilentAuthAvailable(faceId)
    }

    private fun logOut() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            // Jika ada pengguna yang login, lakukan proses log out
            firebaseAuth.signOut()

        }
    }

}