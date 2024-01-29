package com.personal.personalsafetyguard.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.databinding.CreateMasterPasswordBinding

class Register : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding : CreateMasterPasswordBinding
    private var password : String = ""
    private var passwordTemp: String = ""
    private lateinit var masterPassword : String
    private  var count : Int = 0
    private var onConfirmation : Boolean = false
    private lateinit var preference : SharedPreferences
    private val PERMISSIONCODE : Int = 1

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateMasterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        setUpClickListeners()
        preference = getSharedPreferences("PasswordPreferences",Context.MODE_PRIVATE)
        binding.passwordGridLayout.setOnClickListener(this)

        supportActionBar?.hide()

        if(preference.contains("PIN"))
            startActivity(Intent(this,Login::class.java).putExtra("PIN",preference.getString("PIN","No Pin")))
    }

    private fun setUpClickListeners() {
        binding.number0.setOnClickListener(this)
        binding.number1.setOnClickListener(this)
        binding.number2.setOnClickListener(this)
        binding.number3.setOnClickListener(this)
        binding.number4.setOnClickListener(this)
        binding.number5.setOnClickListener(this)
        binding.number6.setOnClickListener(this)
        binding.number7.setOnClickListener(this)
        binding.number8.setOnClickListener(this)
        binding.number9.setOnClickListener(this)
        binding.next.setOnClickListener(this)
        binding.numberDelete.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {
        val readPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        val readPermissionGranted = ActivityCompat.checkSelfPermission(this, readPermission) == PackageManager.PERMISSION_GRANTED
        val writePermissionGranted = ActivityCompat.checkSelfPermission(this, writePermission) == PackageManager.PERMISSION_GRANTED

        if (!readPermissionGranted || !writePermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val permissions = arrayOf(readPermission, writePermission, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSIONCODE)
            } else {
                val permissions = arrayOf(readPermission, writePermission)
                requestPermissions(permissions, PERMISSIONCODE)
            }
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.number1 -> {
                Log.e("Register", "onclick")
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number2 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number3 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number4 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number5 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number6 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number7 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number8 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number9 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.number0 -> {
                view as TextView
                password+= view.text
                count++
                if(count== 4 && !onConfirmation){
                    binding.demoText.text = "Konfirmasi Password"
                    onConfirmation = true
                    uncheckPasswordViews()
                    count = 0
                    passwordTemp = password
                    password = ""
                }
                else if(count == 4 && onConfirmation){
                    if(password == passwordTemp){
                        var editor = preference.edit()
                        editor.putString("PIN",password)
                        editor.commit()
                        val intent = Intent(this,HomeData::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Password tidak sama!",Toast.LENGTH_SHORT).show()
                        password = ""
                        count = 0
                        onConfirmation = true
                        uncheckPasswordViews()
                    }

                }
                else{
                    when(count){
                        1 -> binding.passwordFirst.setImageResource(R.drawable.password_entered)
                        2 -> binding.passwordSecond.setImageResource(R.drawable.password_entered)
                        3 -> binding.passwordThird.setImageResource(R.drawable.password_entered)
                        4 -> binding.passwordFourth.setImageResource(R.drawable.password_entered)
                    }
                }
            }
            R.id.next -> {
                if(password == passwordTemp){
                    var editor = preference.edit()
                    editor.putString("PIN",password)
                    editor.commit()
                    val intent = Intent(this,HomeData::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Password salah",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.numberDelete -> {
                password = password.substring(0,password.length-1)
                when(count){
                    1 -> binding.passwordFirst.setImageResource(R.drawable.password_not_entered)
                    2 -> binding.passwordSecond.setImageResource(R.drawable.password_not_entered)
                    3 -> binding.passwordThird.setImageResource(R.drawable.password_not_entered)
                    4 -> binding.passwordFourth.setImageResource(R.drawable.password_not_entered)
                }
                count--
            }

        }
    }

    private fun uncheckPasswordViews() {
        binding.passwordFirst.setImageResource(R.drawable.password_not_entered)
        binding.passwordSecond.setImageResource(R.drawable.password_not_entered)
        binding.passwordThird.setImageResource(R.drawable.password_not_entered)
        binding.passwordFourth.setImageResource(R.drawable.password_not_entered)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONCODE) {
            if (grantResults.isNotEmpty()) {
                var allPermissionsGranted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }
                if (allPermissionsGranted) {
                    // Permissions granted
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
                } else {
                    // Permissions not granted
                    Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                // Permissions not granted
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
