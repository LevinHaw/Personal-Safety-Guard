package com.personal.personalsafetyguard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.personal.personalsafetyguard.contacts.ContactModel
import com.personal.personalsafetyguard.contacts.CustomAdapter
import com.personal.personalsafetyguard.contacts.DbHelper
import com.personal.personalsafetyguard.shakeServices.ReactivateService
import com.personal.personalsafetyguard.shakeServices.SensorService

class Proteksi : AppCompatActivity() {


    private val IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002
    private val PICK_CONTACT = 1
    private lateinit var button1: Button
    private lateinit var back: ImageButton
    private lateinit var listView: ListView
    private lateinit var db: DbHelper
    private lateinit var list: MutableList<ContactModel>
    private lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proteksi)

        //check for runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                    ),
                    100
                )
            }
        }

        //for populating the dialog with "ALLOW ALL THE TIME" option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 100)
        }

        //check for BatteryOptimization,
        val pm = getSystemService(POWER_SERVICE) as PowerManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                askIgnoreOptimization()
            }
        }

        //start the service
        val sensorService = SensorService()
        val intent = Intent(this, sensorService::class.java)
        if (!isMyServiceRunning(sensorService::class.java)) {
            startService(intent)
        }

        button1 = findViewById(R.id.Button1)
        back = findViewById(R.id.ib_back)
        listView = findViewById(R.id.ListView)
        db = DbHelper(this)
        list = db.getAllContacts().toMutableList()
        customAdapter = CustomAdapter(this, list)
        listView.adapter = customAdapter

        button1.setOnClickListener {
            if (db.count() != 5) {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(intent, PICK_CONTACT)
            } else {
                Toast.makeText(this@Proteksi, "Tidak dapat menambahkan lebih dari 5 kontak", Toast.LENGTH_SHORT).show()
            }
        }

        back.setOnClickListener {
            startActivity(Intent(this@Proteksi, MainActivity::class.java))
        }
    }

    //method to check if the service is running
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, ReactivateService::class.java)
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                    this@Proteksi,
                    "Perizinan ditolak!\n Tidak dapat menggunakan aplikasi ini!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_CONTACT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val contactData: Uri? = data?.data
                    val c: Cursor? = contactData?.let {
                        managedQuery(
                            it,
                            null,
                            null,
                            null,
                            null
                        )
                    }
                    if (c?.moveToFirst() == true) {
                        val id: String =
                            c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val hasPhone: String? =
                            c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        var phone: String? = null
                        try {
                            if (hasPhone == "1") {
                                val phones: Cursor? = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null,
                                    null
                                )
                                phones?.moveToFirst()
                                phone = phones?.getString(phones.getColumnIndexOrThrow("data1"))
                            }
                            val name: String? =
                                c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                            db.addcontact(ContactModel(0, name ?: "", phone ?: ""))
                            list = db.getAllContacts().toMutableList()
                            customAdapter.refresh(list)
                        } catch (ex: Exception) {
                            // Handle exception
                        }
                    }
                }
            }
        }
    }

    private fun askIgnoreOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @SuppressLint("BatteryLife") val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST)
        }
    }
}