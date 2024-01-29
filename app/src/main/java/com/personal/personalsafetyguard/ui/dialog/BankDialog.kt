package com.personal.personalsafetyguard.ui.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.personal.personalsafetyguard.databinding.BankDialogBinding
import com.personal.personalsafetyguard.databinding.LoginDialogBinding
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem
import com.personal.personalsafetyguard.ui.activities.HomeData
import dev.skomlach.biometric.compat.BiometricApi
import dev.skomlach.biometric.compat.BiometricAuthRequest
import dev.skomlach.biometric.compat.BiometricManagerCompat
import dev.skomlach.biometric.compat.BiometricType
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


class BankDialog(private val bankItem : BankDetailsItem) : DialogFragment() {

    private  lateinit var binding : BankDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = BankDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        binding.bankAccountNumberEt.text = bankItem.bankAccNumber.toString()
        binding.bankNameEt.text = bankItem.bankName
        binding.bankAddressEt.text = bankItem.bankAddress
        binding.bankPassEt.text = bankItem.encryptedBankPassword

        binding.btnDecrypt.setOnClickListener {
            if (isFaceBiometricSupported()) {
                showBiometricPrompt(BiometricType.BIOMETRIC_FACE)
            } else {
                Toast.makeText(requireContext(),"verifikasi biometrik tidak tersedia",Toast.LENGTH_SHORT).show()
            }

        }

        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setUpOnClick()

        return builder.create()


    }

    private fun setUpOnClick() {
        binding.copyAccountNumber.setOnClickListener {
            var clipboardManager : ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if(clipboardManager.hasPrimaryClip()){
                var data : ClipData = ClipData.newPlainText("Teks disalin",bankItem.bankAccNumber.toString())
                clipboardManager.setPrimaryClip(data)
            }
            Toast.makeText(requireContext(),"Disalin ke papan klip",Toast.LENGTH_SHORT).show()
        }

        binding.copyAddress.setOnClickListener {
            var clipboardManager : ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if(clipboardManager.hasPrimaryClip()){
                var data : ClipData = ClipData.newPlainText("Teks disalin",bankItem.bankAddress)
                clipboardManager.setPrimaryClip(data)
            }
            Toast.makeText(requireContext(),"Disalin ke papan klip",Toast.LENGTH_SHORT).show()
        }


        binding.copyBankName.setOnClickListener {
            var clipboardManager : ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if(clipboardManager.hasPrimaryClip()){
                var data : ClipData = ClipData.newPlainText("Teks disalin",bankItem.bankName)
                clipboardManager.setPrimaryClip(data)
            }
            Toast.makeText(requireContext(),"Disalin ke papan klip",Toast.LENGTH_SHORT).show()
        }

    }

    private fun decryptData(encryptedData: String): String {


        try {
            val sharedPreferences = EncryptedSharedPreferences.create(
                "encrypted_shared_prefs",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                requireContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val encryptedKey = sharedPreferences.getString("encrypted_key", null)
            val encryptedIV = sharedPreferences.getString("encrypted_iv", null)

            if (encryptedKey != null && encryptedIV != null) {
                val keyBytes = Base64.decode(encryptedKey, Base64.DEFAULT)
                val ivBytes = Base64.decode(encryptedIV, Base64.DEFAULT)

                val key = SecretKeySpec(keyBytes, "AES")
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                val parameterSpec = GCMParameterSpec(128, ivBytes)
                cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec)

                val decryptedBytes = cipher.doFinal(hexToBytes(encryptedData))
                return String(decryptedBytes)
            }
        } catch (e: AEADBadTagException) {
            // Handle decryption failure due to bad tag exception
            // For example, you can log the error or show an error message to the user
            e.printStackTrace()
            // Return null or an empty string to indicate decryption failure
            return ""
        } catch (e: Exception) {
            // Handle other exceptions that might occur during decryption
            e.printStackTrace()
            // Return null or an empty string to indicate decryption failure
            return ""
        }

        // Return null or an empty string to indicate decryption failure
        return ""
    }
    private fun hexToBytes(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) {
            val index = i * 2
            val byteStr = hexString.substring(index, index + 2)
            result[i] = byteStr.toInt(16).toByte()
        }
        return result
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

    private fun showBiometricPrompt(biometricType: BiometricType) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autentikasi biometrik")
            .setSubtitle("Login menggunakan biometrik ${biometricType.name.toLowerCase()} ")
            .setNegativeButtonText("Batal")
            .build()

        val biometricPrompt = BiometricPrompt(
            this, ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                    Toast.makeText(requireContext(),"Autentikasi error: $errString",Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Handle authentication success
                    val decryptedPassword = bankItem.bankPass
                    binding.bankPassEt.setText(decryptedPassword)
                    Toast.makeText(requireContext(),"Autentikasi sukses dengan biometrik",Toast.LENGTH_SHORT).show()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                    Toast.makeText(requireContext(),"Autentikasi gagal dengan biometrik",Toast.LENGTH_SHORT).show()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

}