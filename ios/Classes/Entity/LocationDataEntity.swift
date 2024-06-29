import Foundation

struct LocationDataEntity: Codable {
    var latitude: Double
    var longitude: Double
    var accuracy: Double
    var timestamp: Int64
    var mockLocation: Bool
}

func decodeLocationDataEntities(from data: Data) -> [LocationDataEntity] {
    let decoder = JSONDecoder()
    return (try? decoder.decode([LocationDataEntity].self, from: data)) ?? []
}

func encodeLocationDataEntities(from locations: [LocationDataEntity]) -> Data {
    let encoder = JSONEncoder()
    return (try? encoder.encode(locations)) ?? Data()
}
