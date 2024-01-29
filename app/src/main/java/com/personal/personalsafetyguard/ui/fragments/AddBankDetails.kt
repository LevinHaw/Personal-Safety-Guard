package com.personal.personalsafetyguard.ui.fragments

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.personal.personalsafetyguard.databinding.AddBankBinding
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


@AndroidEntryPoint
class AddBankDetails : Fragment(){
    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: AddBankBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddBankBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        binding.addBankDetails.setOnClickListener {
            var bankAccNumber = binding.bankAccountNumberEt.text.toString().trim()
            var bankName      = binding.bankNameEt.text.toString().trim()
            var bankAddress   = binding.bankAddressEt.text.toString().trim()
            var bankPass      = binding.bankPassEt.text.toString().trim()
            var encryptedBankPassword = encryptData(bankPass)

            if(valid(bankName,bankAccNumber,bankPass,bankAddress)){
                viewModel.insertBankDetails(BankDetailsItem(bankName,bankAccNumber.toLong(),bankPass,bankAddress, encryptedBankPassword))
                Toast.makeText(context,"Detail telah dimasukkan",Toast.LENGTH_SHORT).show()
                val action = AddBankDetailsDirections.actionAddBankDetailsToBankDetails()
                findNavController().navigate(action)
            }
            else
                Toast.makeText(context,"Tolong isi semua bagian yang kosong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun valid(bankName : String, bankAccNumber : String,  bankPass: String, bankAddress: String) : Boolean{
        return !(bankAccNumber.isEmpty() || bankAddress.isEmpty() || bankName.isEmpty()
                || bankPass.isEmpty())
    }

    private fun encryptData(data: String): String {
        val key = generateKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val parameterSpec = GCMParameterSpec(128, generateIV())
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec)

        val encryptedData = cipher.doFinal(data.toByteArray())

        val encryptedKey = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        val encryptedIV = Base64.encodeToString(parameterSpec.iv, Base64.DEFAULT)

        saveKeyAndIVToSharedPreferences(encryptedKey, encryptedIV)

        return bytesToHex(encryptedData)
    }

    private fun saveKeyAndIVToSharedPreferences(encryptedKey: String, encryptedIV: String) {
        val sharedPreferences = EncryptedSharedPreferences.create(
            "encrypted_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.putString("encrypted_key", encryptedKey)
        editor.putString("encrypted_iv", encryptedIV)
        editor.apply()
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256, SecureRandom())
        return keyGenerator.generateKey()
    }

    private fun generateIV(): ByteArray {
        val iv = ByteArray(12)
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(iv)
        return iv
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}