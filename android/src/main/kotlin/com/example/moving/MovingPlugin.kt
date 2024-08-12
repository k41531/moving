package com.example.moving

import StepCounter
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** MovingPlugin */
class MovingPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var stepCounter: StepCounter
    private lateinit var distanceMeter: DistanceMeter

    companion object {
        private const val TAG = "MovingPlugin"
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "moving")
        channel.setMethodCallHandler(this)
        stepCounter = StepCounter(flutterPluginBinding.applicationContext)
        distanceMeter = DistanceMeter(flutterPluginBinding.applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "subscribeRecording" -> {
                stepCounter.subscribeRecording(
                    onSuccess = {
                        result.success(true)
                    },
                    onError = { errorCode, errorMessage ->
                        result.error(errorCode, errorMessage, null)
                    }
                )
            }
            "getTodaySteps" -> {
                stepCounter.getTodaySteps(
                    onSuccess = { steps ->
                        result.success(steps)
                    },
                    onError = { errorCode, errorMessage ->
                        result.error(errorCode, errorMessage, null)
                    }
                )
            }
            "getTodayDistance" -> {
                distanceMeter.getTodayDistance(
                    onSuccess = { distance ->
                        result.success(distance)
                    },
                    onError = { errorCode, errorMessage ->
                        result.error(errorCode, errorMessage, null)
                    }
                )
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
