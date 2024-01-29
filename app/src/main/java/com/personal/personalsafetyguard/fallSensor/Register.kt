package com.personal.personalsafetyguard.fallSensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.personal.personalsafetyguard.FallTracker
import com.personal.personalsafetyguard.R

class Register : AppCompatActivity() {

    var mobile_number_input: EditText? = null
    var mobile_name_input: EditText? = null
    var save_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

         mobile_number_input = findViewById(R.id.mobile_number_input)
         mobile_name_input = findViewById(R.id.mobile_name_input)
         save_btn = findViewById(R.id.save_btn)

        // Save Button Click Listener
        save_btn?.setOnClickListener(View.OnClickListener {
            val mobile_number = mobile_number_input?.getText().toString()
            val mobile_name = mobile_name_input?.getText().toString()
            if (mobile_name.length > 15) {
                Toast.makeText(
                    this@Register,
                    "Masukkan nama ponsel kurang dari 15 karakter.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mobile_name == "" || mobile_number == "") {
                Toast.makeText(this@Register, "Kolom input tidak boleh kosong !", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
                val spEditor = sharedPreferences.edit()
                spEditor.putString("mobile_number", mobile_number)
                spEditor.putString("mobile_name", mobile_name)
                spEditor.apply()
                val intent = Intent(this@Register, FallTracker::class.java)
                startActivity(intent)
            }
        })

    }

    override fun onBackPressed() {
        val intent = Intent(this@Register, FallTracker::class.java)
        intent.putExtra("isFinish", true)
        startActivity(intent)
    }
}