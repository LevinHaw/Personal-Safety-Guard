package com.personal.personalsafetyguard.fallSensor

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.personal.personalsafetyguard.FallTracker
import com.personal.personalsafetyguard.R
import java.io.IOException
import java.util.Locale

class FallService : Service(), SensorEventListener {

    var mediaPlayer: MediaPlayer? = null
    var notificationManager: NotificationManager? = null

    // Location
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //Phone Fall detection
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        setNotification(
            NOTIFICATION_ID,
            NOTIFICATION_REQ_CODE,
            CHANNEL_ID,
            "Fitur pelcakan sedang berjalan.",
            "Personal Safety Guard App",
            1
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaPlayer!!.stop()
            notificationManager!!.cancelAll()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Maaf gagal mematikan nada dering. Nada dering akan berhenti secara otomatis setelah beberapa detik!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val xValue = event.values[0]
        val yValue = event.values[1]
        val zValue = event.values[2]
        val rootSquare = Math.sqrt(
            Math.pow(xValue.toDouble(), 2.0) + Math.pow(
                yValue.toDouble(),
                2.0
            ) + Math.pow(zValue.toDouble(), 2.0)
        )
        if (rootSquare < 2.0) {
            setNotification(
                200,
                200,
                CHANNEL_ID,
                "Personal Safety Guard : Berdering.",
                "Handphone berdering !",
                2
            )
            mediaPlayer!!.isLooping = false
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer) {
//                 Code for fetching location and sending message
                    lastLocation
                    notificationManager!!.cancelAll()
                    stopSelf()
                    val sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE)
                    val spEditor = sharedPreferences.edit()
                    spEditor.putBoolean("is_service_running", false)
                    spEditor.apply()
                }
            })
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    fun setNotification(
        notification_id: Int,
        notification_req_code: Int,
        channel_id: String?,
        cText: String?,
        sText: String?,
        flag: Int
    ) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification
        val notificationIntent: Intent
        notificationIntent = if (flag == 1) {
            Intent(this, FallTracker::class.java)
        } else {
            Intent(this, RingAlarm::class.java)
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notification_req_code,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, channel_id)
                .setSmallIcon(R.drawable.ic_baseline_miscellaneous_services_24)
                .setContentText(cText)
                .setSubText(sText)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build()
            notificationManager!!.createNotificationChannel(
                NotificationChannel(
                    channel_id,
                    "All Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        } else {
            notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_miscellaneous_services_24)
                .setContentText("Fitur sedang berjalan.")
                .setSubText("")
                .setContentIntent(pendingIntent)
                .build()
        }
        notificationManager!!.notify(notification_id, notification)
    }

    // Getting data from shared preferences
    private val lastLocation: Unit
        private get() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient!!.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val geocoder = Geocoder(this@FallService, Locale.getDefault())
                            try {
                                val address = geocoder.getFromLocation(
                                    location.latitude,
                                    location.longitude,
                                    1
                                )
                                val current_latitude = address!![0].latitude.toString()
                                val current_longitude = address[0].longitude.toString()

                                // Getting data from shared preferences
                                val sharedPreferences =
                                    getSharedPreferences("SharedPref", MODE_PRIVATE)
                                val mobile_number =
                                    sharedPreferences.getString("mobile_number", null)
                                val mobile_name = sharedPreferences.getString("mobile_name", null)
                                if (current_latitude != null && current_longitude != null) {
                                    val url =
                                        "https://www.google.com/maps/search/?api=1&query=$current_latitude,$current_longitude"
                                    val message =
                                        "Hei, ponsel $mobile_name Anda terjatuh dan tidak ada yang mengangkatnya setelah satu menit."
                                    val message2 = "Lokasi jatuh : $url"
                                    try {
                                        sendSms(mobile_number, message)
                                        sendSms(mobile_number, message2)
                                        Toast.makeText(
                                            this@FallService,
                                            "Pesan berhasil dikirim !",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        Toast.makeText(
                                            this@FallService,
                                            "Ada masalah saat mengirim pesan ke nomor telepon.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@FallService,
                                        "Ada masalah saat mendapatkan lokasi ponsel.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(
                                this@FallService,
                                "Lokasi Anda tidak diaktifkan atau terjadi masalah lain!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Mohon berikan izin lokasi !", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun sendSms(mobile_no: String?, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(mobile_no, null, message, null, null)
    }

    companion object {
        const val NOTIFICATION_REQ_CODE = 100
        const val CHANNEL_ID = "Service Channel"
        const val NOTIFICATION_ID = 100
        const val LOCATION_PERMISSION_ID = 108
        var phone_fall = false
    }
}