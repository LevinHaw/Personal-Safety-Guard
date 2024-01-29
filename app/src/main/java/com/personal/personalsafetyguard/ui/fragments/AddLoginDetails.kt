package com.personal.personalsafetyguard.ui.fragments

import android.R
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.personal.personalsafetyguard.databinding.AddLoginBinding
import com.personal.personalsafetyguard.model.LoginDetailsItem
import com.personal.personalsafetyguard.ui.dialog.PasswordGeneratorDialog
import com.personal.personalsafetyguard.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC

@AndroidEntryPoint
class AddLoginDetails : Fragment(),AdapterView.OnItemSelectedListener,PasswordGeneratorDialog.OnCopyListener{
    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: AddLoginBinding
    private val categoryList: ArrayList<String> = ArrayList()
    private var category : String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddLoginBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        initSpinner()

        binding.addLoginDetails.setOnClickListener {
            var loginName       = binding.loginNameEt.text.toString().trim()
            var loginEmail      = binding.loginEmailEt.text.toString().trim()
            var loginPassword   = binding.loginPasswordEt.text.toString().trim()
            var loginWebsite    = binding.loginWebsiteEt.text.toString().trim()
            var loginNotes      = binding.loginNoteEt.text.toString().trim()
            var encryptedPassword = encryptData(loginPassword)


            if(loginWebsite.isEmpty())
                loginWebsite = " "
            if(loginNotes.isEmpty())
                loginNotes  = " "

            if(valid(loginName,loginEmail,loginPassword)){
                val id = loginName + loginEmail.split("@")[0]
                viewModel.insertLoginDetails(LoginDetailsItem(id,loginName,loginEmail,loginPassword,loginWebsite,loginNotes,category, encryptedPassword))
                Toast.makeText(context,"Detail telah dimasukkan",Toast.LENGTH_SHORT).show()
                val action = AddLoginDetailsDirections.actionAddLoginDetailsToLoginDetails()
                findNavController().navigate(action)
            }
            else
                Toast.makeText(context,"Tolong isi semua data",Toast.LENGTH_SHORT).show()
        }

        binding.generatePassword.setOnClickListener {
            val dialog = PasswordGeneratorDialog()
            dialog.show(childFragmentManager,"Dialog")
        }
    }

    private fun initSpinner() {
        categoryList.add("Categori")
        categoryList.add("Sosial")
        categoryList.add("Pekerjaan")
        categoryList.add("Games")
        categoryList.add("Alternatif")
        categoryList.add("E-Commerce")
        categoryList.add("Lainnya")

        val adapter  = ArrayAdapter<String> (requireContext(),R.layout.simple_spinner_dropdown_item
            ,categoryList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.loginCategorySpinner.adapter = adapter
        binding.loginCategorySpinner.onItemSelectedListener = this
    }


    private fun valid(loginName : String, loginEmail : String,  loginPassword: String) : Boolean{

        return !(loginName.isEmpty() || loginEmail.isEmpty() || loginPassword.isEmpty() || category.isEmpty())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p2 != 0)
            category = categoryList[p2]
    }

    override fun sendPassword(password: String) {
        binding.loginPasswordEt.setText(password)
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

    private fun saveKeyAndIVToSharedPreferences(encryptedAccountKey: String, encryptedAccountIV: String) {
        val sharedPreferences = EncryptedSharedPreferences.create(
            "encrypted_shared_prefs",
            MasterKeys.getOrCreate(AES256_GCM_SPEC),
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.putString("encrypted_account_key", encryptedAccountKey)
        editor.putString("encrypted_account_iv", encryptedAccountIV)
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