package com.example.moving

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId

class DistanceMeter(private val context: Context) {
    companion object {
        private  const val TAG = "DistanceMeter"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayDistance(onSuccess: (Int) -> Unit, onError: (String, String?) -> Unit) {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusWeeks(1)

        val distance = 0
        onSuccess(distance)
    }

    fun startLocationService() {
        Log.d("moving", "TEST")
        val intent = Intent(context, LocationService::class.java)
        context.stopService(intent)

//        LocationUpdatesService.locationLiveData.observeForever(locationObserver)
//        LocationUpdatesService.statusLiveData.observeForever(statusObserver)
//
        context.startService(intent)
        Log.d("moving", "Start")

    }

}