package com.example.ecommerce

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.ecommerce.core.data.database.cart.CartDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

    @Inject
    lateinit var database: CartDatabase

    private val mainViewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.sessionExpired.observe(this@MainActivity) { response ->
            if (response != null && response == true) {
                logOut()
                mainViewModel.resetSession()
                Toast.makeText(
                    this,
                    getString(R.string.sesi_anda_telah_berakhir),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        askNotificationPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW,
                ),
            )
        }
    }

    fun goToCart() {
        navController.navigate(R.id.action_mainFragment_to_cartFragment)
    }

    fun goToNotification() {
        navController.navigate(R.id.action_mainFragment_to_notificationFragment)
    }

    fun goToMore() {
        navController.navigate(R.id.action_mainFragment_to_screenFragment)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun logOut() {
        navController.navigate(R.id.main_to_prelogin)
        mainViewModel.deleteToken()
        GlobalScope.launch {
            database.clearAllTables()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
