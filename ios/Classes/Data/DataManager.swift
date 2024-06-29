class DataManager {
        
        private let userDefaults: UserDefaults
        
        init(userDefaults: UserDefaults) {
            self.userDefaults = userDefaults
        }
        
        func saveLocation(location: LocationDataEntity) {
            var locations = getStoredLocations()
            locations.append(location)
            let data = encodeLocationDataEntities(from: locations)
            userDefaults.set(data, forKey: "locations")
        }
        
        func saveDistance(distance: DistanceEntity) {
            let data = encodeDistanceEntity(from: distance)
            userDefaults.set(data, forKey: "distance")
        }
        
        func getStoredLocations() -> [LocationDataEntity] {
            guard let data = userDefaults.data(forKey: "locations") else {
                return []
            }
            
            return decodeLocationDataEntities(from: data)
            
        }
        
        func getStoredDistance() -> DistanceEntity {
            guard let data = userDefaults.data(forKey: "distance") else {
                return DistanceEntity(totalDistance: 0, todayDistance: 0, lastUpdated: "")
            }
            
            return decodeDistanceEntity(from: data)
        }
        
        func clearData() {
            userDefaults.removeObject(forKey: "locations")
            userDefaults.removeObject(forKey: "distance")
        }
    }
