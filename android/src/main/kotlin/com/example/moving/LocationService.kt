package com.example.moving

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.flutter.Log

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: String = ""


    private val pref: SharedPreferences
        get() = applicationContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_FILE_NAME = "MOVING_DATA"
    }

    override fun onCreate() {
        super.onCreate()
        initializeLocationClient()
        createLocationCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("moving", "start location service")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, createNotification(), FOREGROUND_SERVICE_TYPE_LOCATION)
        }else{
            startForeground(1, createNotification())
        }
        startLocationUpdates()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun stopService(name: Intent?): Boolean {
        stopLocationUpdates()
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        stopSelf()
    }

    private fun initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun createLocationCallback() {
        var updatedCount = 0
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d("moving", "[${updatedCount}] location: ${locationResult.lastLocation}")
                currentLocation = "${locationResult.lastLocation}"
                updatedCount++
                pref.edit().putString("currentLocation", currentLocation).apply()
            }
        }
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "location_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Location Service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This channel is used by location service"
            }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Location Service")
            .setContentText("Location Service is running in the background")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
            .build()
    }

    private fun startLocationUpdates() {
        Log.d("moving", "start location update")
        val locationRequest = createLocationRequest()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        Log.d("moving", "started location update")
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            5000
        ).build()
    }
}
