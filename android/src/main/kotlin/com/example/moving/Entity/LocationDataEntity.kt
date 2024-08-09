import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class LocationDataEntity(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
    val timestamp: Int,
    val mockLocation: Boolean
)

fun decodeLocationDataEntity(data: ByteArray): LocationDataEntity {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        json.decodeFromString(LocationDataEntity.serializer(), data.decodeToString())
    } catch (e: Exception) {
        LocationDataEntity(latitude = 0.0, longitude = 0.0, accuracy = 0.0, timestamp = 0, mockLocation = false)
    }
}

fun encodeLocationDataEntity(locationData: LocationDataEntity): ByteArray {
    val json = Json { prettyPrint = true }
    return try {
        json.encodeToString(LocationDataEntity.serializer(), locationData).encodeToByteArray()
    } catch (e: Exception) {
        ByteArray(0)
    }
}

