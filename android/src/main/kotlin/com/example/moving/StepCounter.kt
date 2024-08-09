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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class StepCounter(private val context: Context) {
    companion object {
        private const val TAG = "StepCounter"
    }

    fun subscribeRecording(onSuccess: () -> Unit, onError: (String, String?) -> Unit) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val hasMinPlayServices = googleApiAvailability.isGooglePlayServicesAvailable(
            context,
            LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE
        )

        if (hasMinPlayServices != ConnectionResult.SUCCESS) {
            onError("RECORDING_ERROR", "Google Play Services is not available")
            return
        }
        Log.d(TAG, "Google Play Services is available")

        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        localRecordingClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully subscribed to step count recording")
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("RECORDING_ERROR", "Failed to subscribe to step count recording: ${e.message}")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodaySteps(onSuccess: (Int) -> Unit, onError: (String, String?) -> Unit) {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusWeeks(1)
        val readRequest = LocalDataReadRequest.Builder()
            .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
            .build()

        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)

        localRecordingClient.readData(readRequest).addOnSuccessListener { response ->
            var steps = 0
            for (dataSet in response.buckets.flatMap { it.dataSets }) {
                dumpDataSet(dataSet)
                steps += getSteps(dataSet)
            }
            onSuccess(steps)
        }.addOnFailureListener { e ->
            Log.w(TAG, "There was an error reading data", e)
            onError("READ_ERROR", "Failed to read data: ${e.message}")
        }
    }

    private fun dumpDataSet(dataSet: LocalDataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            Log.i(TAG, "\tType: ${dp.dataType.name}")
            Log.i(TAG, "\tStart: ${dp.getStartTime(TimeUnit.HOURS)}")
            Log.i(TAG, "\tEnd: ${dp.getEndTime(TimeUnit.HOURS)}")
            for (field in dp.dataType.fields) {
                Log.i(
                    TAG,
                    "\tLocalField: ${field.name} LocalValue: ${dp.getValue(field)}"
                )
            }
        }
    }

    private fun getSteps(dataSet: LocalDataSet): Int {
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
}