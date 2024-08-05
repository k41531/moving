package com.example.moving

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** MovingPlugin */
class MovingPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "moving")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "subscribeRecording") {
      subscribeRecording(result)
    } else {
      result.notImplemented()
    }
  }


  private fun subscribeRecording(result: Result) {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val hasMinPlayServices = googleApiAvailability.isGooglePlayServicesAvailable(context, LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE)

    if(hasMinPlayServices != ConnectionResult.SUCCESS) {
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
        result.error("RECORDING_ERROR", "Failed to subscribe to step count recording", e.message)
      }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
