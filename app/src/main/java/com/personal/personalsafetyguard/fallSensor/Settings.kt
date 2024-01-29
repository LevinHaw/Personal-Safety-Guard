package com.personal.personalsafetyguard.fallSensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.personal.personalsafetyguard.FallTracker
import com.personal.personalsafetyguard.R

class Settings : AppCompatActivity() {

    var mobile_number_input: EditText? = null
    var mobile_name_input: EditText? = null
    var save_btn: Button? = null
    var back_btn: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()

        mobile_number_input = findViewById(R.id.mobile_number_input)
        mobile_name_input = findViewById(R.id.mobile_name_input)
        save_btn = findViewById(R.id.save_btn)
        back_btn = findViewById(R.id.back_btn)

        // Getting shared Preferences Data
        val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
        val mobile_number = sharedPreferences.getString("mobile_number", null)
        val mobile_name = sharedPreferences.getString("mobile_name", null)

        // Adding text to mobile number input
        mobile_number_input?.setText(mobile_number)
        mobile_name_input?.setText(mobile_name)

//        Click Listeners
        back_btn?.setOnClickListener(View.OnClickListener { onBackPressed() })
        save_btn?.setOnClickListener(View.OnClickListener {
            val phone_no = mobile_number_input?.getText().toString()
            val phone_name = mobile_name_input?.getText().toString()
            if (mobile_name != null && mobile_name.length > 15) {
                Toast.makeText(
                    this@Settings,
                    "Masukkan nama ponsel kurang dari 15 karakter.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (phone_name == "" || phone_no == "") {
                Toast.makeText(this@Settings, "Kolom input tidak boleh kosong !", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
                val spEditor = sharedPreferences.edit()
                spEditor.putString("mobile_number", phone_no)
                spEditor.putString("mobile_name", phone_name)
                spEditor.apply()
                val intent = Intent(this@Settings, FallTracker::class.java)
                startActivity(intent)
            }
        })
    }
}
