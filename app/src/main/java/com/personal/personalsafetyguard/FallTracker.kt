package com.personal.personalsafetyguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.personal.personalsafetyguard.fallSensor.FallService
import com.personal.personalsafetyguard.fallSensor.Register
import com.personal.personalsafetyguard.fallSensor.Settings

class FallTracker : AppCompatActivity() {

    var tracking_btn: Button? = null
    var settings_btn: Button? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fall_tracker)

        supportActionBar?.hide()

        tracking_btn = findViewById(R.id.tracking_btn)
        settings_btn = findViewById(R.id.settings_btn)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Getting location permission from user
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else askLocationPermission()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else askSmsPermission()

        // Getting SharedPreferences to check mobile number is already added or not
        val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
        val mobile_number = sharedPreferences.getString("mobile_number", null)
        if (mobile_number == null) {
            val intent = Intent(this@FallTracker, Register::class.java)
            startActivity(intent)
        }

        // Click Listeners
        settings_btn?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FallTracker, Settings::class.java)
            startActivity(intent)
        })
        tracking_btn?.setOnClickListener(View.OnClickListener {
            val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
            val is_service_running = sharedPreferences.getBoolean("is_service_running", false)
            val spEditor = sharedPreferences.edit()
            if (is_service_running == false) {
                startService(Intent(this@FallTracker, FallService::class.java))
                spEditor.putBoolean("is_service_running", true)
                spEditor.apply()
                tracking_btn?.setText("Berhenti melacak")
            } else {
                stopService(Intent(this@FallTracker, FallService::class.java))
                spEditor.putBoolean("is_service_running", false)
                spEditor.apply()
                tracking_btn?.setText("Mulai melacak")
            }
        })

    }

    private fun askLocationPermission() {
        ActivityCompat.requestPermissions(
            this@FallTracker,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_ID
        )
    }

    private fun askSmsPermission() {
        ActivityCompat.requestPermissions(
            this@FallTracker,
            arrayOf(Manifest.permission.SEND_SMS),
            LOCATION_PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Mohon Berikan Izin yang diperlukan.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val LOCATION_PERMISSION_ID = 108
    }

}