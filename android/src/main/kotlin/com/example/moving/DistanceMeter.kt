package com.example.moving

import android.content.Context
import android.os.Build
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

}