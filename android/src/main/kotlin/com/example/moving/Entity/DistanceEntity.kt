import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class DistanceEntity(
    val totalDistance: Double,
    val todayDistance: Double,
    val lastUpdated: String
)

fun decodeDistanceEntity(data: ByteArray): DistanceEntity {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        json.decodeFromString(DistanceEntity.serializer(), data.decodeToString())
    } catch (e: Exception) {
        DistanceEntity(totalDistance = 0.0, todayDistance = 0.0, lastUpdated = "")
    }
}

fun encodeDistanceEntity(distance: DistanceEntity): ByteArray {
    val json = Json { prettyPrint = true }
    return try {
        json.encodeToString(DistanceEntity.serializer(), distance).encodeToByteArray()
    } catch (e: Exception) {
        ByteArray(0)
    }
}