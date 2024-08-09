package com.example.moving

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataSet
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.LocalDataReadRequest
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


/** MovingPlugin */
class MovingPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    companion object {
        private const val TAG = "MovingPlugin"
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "moving")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "subscribeRecording") {
            subscribeRecording(result)
        } else if (call.method == "getTodaySteps") {
            getTodaySteps(result)
        } else {
            result.notImplemented()
        }
    }


    private fun subscribeRecording(result: Result) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val hasMinPlayServices = googleApiAvailability.isGooglePlayServicesAvailable(
            context,
            LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE
        )

        if (hasMinPlayServices != ConnectionResult.SUCCESS) {
            result.error("RECORDING_ERROR", "Google Play Services is not available", null)
        }
        Log.d("MovingPlugin", "Google Play Services is available")

        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        // Subscribe to steps data
        localRecordingClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.d("MovingPlugin", "Successfully subscribed to step count recording")
                result.success(true)
            }
            .addOnFailureListener { e ->
                result.error(
                    "RECORDING_ERROR",
                    "Failed to subscribe to step count recording",
                    e.message
                )
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodaySteps(result: Result) {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusWeeks(1)
        val readRequest =
            LocalDataReadRequest.Builder()
                // The data request can specify multiple data types to return,
                // effectively combining multiple data queries into one call.
                // This example demonstrates aggregating only one data type.
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                // Analogous to a "Group By" in SQL, defines how data should be
                // aggregated. bucketByTime allows bucketing by time span.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        var steps = 0

        localRecordingClient.readData(readRequest).addOnSuccessListener { response ->
            // The aggregate query puts datasets into buckets, so flatten into a
            // single list of datasets.
            for (dataSet in response.buckets.flatMap { it.dataSets }) {
                dumpDataSet(dataSet)
                steps += getSteps(dataSet)
            }
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data", e)
            }
        result.success(steps)

    }

    fun dumpDataSet(dataSet: LocalDataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            Log.i(TAG, "\tType: ${dp.dataType.name}")
            Log.i(TAG, "\tStart: ${dp.getStartTime(TimeUnit.HOURS)}")
            Log.i(TAG, "\tEnd: ${dp.getEndTime(TimeUnit.HOURS)}")
            for (field in dp.dataType.fields) {
                Log.i(
                    TAG,
                    "\tLocalField: ${field.name.toString()} LocalValue: ${dp.getValue(field)}"
                )
            }
        }
    }

    fun getSteps(dataSet: LocalDataSet): Int {
        var steps = 0
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                if (field.name == "steps") {
                    steps += dp.getValue(field).asInt()
                }
            }
        }
        return steps
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
