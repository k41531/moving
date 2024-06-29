import Foundation

struct DistanceEntity: Codable {
    var totalDistance: Double
    var todayDistance: Double
    var lastUpdated: String
}

func decodeDistanceEntity(from data: Data) -> DistanceEntity {
    let decoder = JSONDecoder()
    return (try? decoder.decode(DistanceEntity.self, from: data)) ?? DistanceEntity(totalDistance: 0, todayDistance: 0, lastUpdated: "")
}

func encodeDistanceEntity(from distance: DistanceEntity) -> Data {
    let encoder = JSONEncoder()
    return (try? encoder.encode(distance)) ?? Data()
}
