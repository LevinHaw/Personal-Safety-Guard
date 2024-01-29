package com.personal.personalsafetyguard.shakeServices

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.personal.personalsafetyguard.contacts.ContactModel
import com.personal.personalsafetyguard.contacts.DbHelper
import com.personal.personalsafetyguard.R
import com.google.android.gms.tasks.CancellationTokenSource

class SensorService : Service() {
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground()
        } else {
            startForeground(1, Notification())
        }

        val cancellationTokenSource = CancellationTokenSource()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            @SuppressLint("MissingPermission")
            override fun onShake(count: Int) {
                if (count == 3) {
                    vibrate()

                    val fusedLocationClient: FusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(applicationContext)
                    fusedLocationClient.requestLocationUpdates(
                        LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
                        object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                super.onLocationResult(locationResult)
                                val location = locationResult?.lastLocation
                                if (location != null) {
                                    val smsManager = SmsManager.getDefault()
                                    val db = DbHelper(this@SensorService)
                                    val list: List<ContactModel> = db.getAllContacts()
                                    for (c in list) {
                                        val message =
                                            """${c.name}, saya butuh bantuan. Cepat cari saya. Saya berada di
                           |http://maps.google.com/?q=${location.latitude},${location.longitude}""".trimMargin()
                                        smsManager.sendTextMessage(
                                            c.phoneNo,
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    }
                                } else {
                                    val message =
                                        """Saya butuh bantuan. Tolong segera hubungi saya.
                       |GPS tidak di izinkan dan tidak dapat menemukan lokasi.""".trimMargin()
                                    val smsManager = SmsManager.getDefault()
                                    val db = DbHelper(this@SensorService)
                                    val list: List<ContactModel> = db.getAllContacts()
                                    for (c in list) {
                                        smsManager.sendTextMessage(
                                            c.phoneNo,
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    }
                                }
                            }

                            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                                super.onLocationAvailability(locationAvailability)
                                if (!locationAvailability?.isLocationAvailable!!) {
                                    val message =
                                        """Saya dalam BAHAYA, saya butuh bantuan. Tolong segera hubungi saya.
                       |GPS dimatikan. Tidak dapat menemukan lokasi. Hubungi Kantor Polisi terdekat..""".trimMargin()
                                    val smsManager = SmsManager.getDefault()
                                    val db = DbHelper(this@SensorService)
                                    val list: List<ContactModel> = db.getAllContacts()
                                    for (c in list) {
                                        smsManager.sendTextMessage(
                                            c.phoneNo,
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    }
                                }
                            }
                        },
                        null
                    )
                }
            }
        })

        mSensorManager?.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private fun vibrate() {
        val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val vibEff: VibrationEffect

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibEff = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
            vibrator.cancel()
            vibrator.vibrate(vibEff)
        } else {
            vibrator.vibrate(500)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        val manager: NotificationManager =
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)!!
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Kiriman bantuan sudah terkirim.")
            .setContentText("Kami ada 24 jam")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onDestroy() {
        val broadcastIntent = Intent()
        broadcastIntent.setAction("restartservice")
        broadcastIntent.setClass(this, ReactivateService::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }
}
