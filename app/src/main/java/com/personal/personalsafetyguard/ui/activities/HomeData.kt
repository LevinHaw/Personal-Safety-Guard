package com.personal.personalsafetyguard.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.databinding.ActivityHomeDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeData : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeDataBinding
    private lateinit var listener: NavController.OnDestinationChangedListener

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        Navigation.setViewNavController(binding.cardsDetails, navController)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.cardsDetails.setOnClickListener {
            navController.navigate(R.id.action_global_addCardDetails)
            binding.floatingMenu.close(true)
        }

        binding.loginDetails.setOnClickListener {
            navController.navigate(R.id.action_global_addLoginDetails)
            binding.floatingMenu.close(true)
        }

        binding.bankDetails.setOnClickListener {
            navController.navigate(R.id.action_global_addBankDetails)
            binding.floatingMenu.close(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        disableScreenCapture()
    }

    override fun onResume() {
        super.onResume()
        enableScreenCapture()
    }

    private fun disableScreenCapture() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun enableScreenCapture() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}