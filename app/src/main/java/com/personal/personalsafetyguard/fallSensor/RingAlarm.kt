package com.personal.personalsafetyguard.fallSensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.personal.personalsafetyguard.FallTracker
import com.personal.personalsafetyguard.R

class RingAlarm : AppCompatActivity() {

    var stop_ringing_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring_alarm)

        stop_ringing_btn = findViewById(R.id.stop_ringing_btn)

        //Click Listeners
        stop_ringing_btn?.setOnClickListener(View.OnClickListener {
            stopService(Intent(this@RingAlarm, FallService::class.java))
            val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
            val spEditor = sharedPreferences.edit()
            spEditor.putBoolean("is_service_running", false)
            spEditor.apply()
            val intent = Intent(this@RingAlarm, FallTracker::class.java)
            startActivity(intent)
        })
    }

    companion object {
        var isStopRinging = false
    }

}
