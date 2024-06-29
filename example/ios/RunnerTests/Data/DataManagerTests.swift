import XCTest
import CoreLocation

@testable import moving

class DataManagerTests: XCTestCase {
    var dataManager: DataManager!
    
    override func setUp() {
        super.setUp()
        let userDefaults = UserDefaults(suiteName: "Moving")
        userDefaults?.removePersistentDomain(forName: "Moving")
        dataManager = DataManager(userDefaults: userDefaults!)
    }
    
    override func tearDown() {
        dataManager = nil
        super.tearDown()
    }
    
    func testSaveLocation() {
        let location = LocationDataEntity(
            latitude: 37.7749,
            longitude: -122.4194,
            accuracy: 1.0,
            timestamp: 0,
            mockLocation: true
        )
        dataManager.saveLocation(location: location)
        
        let storedLocations = dataManager.getStoredLocations()
        XCTAssertEqual(storedLocations.count, 1)
        XCTAssertEqual(storedLocations[0].latitude, 37.7749)
        XCTAssertEqual(storedLocations[0].longitude, -122.4194)
    }

    func testSaveDistance() {
        let distance = DistanceEntity(
            totalDistance: 1000,
            todayDistance: 0,
            lastUpdated: ""
        )
        dataManager.saveDistance(distance: distance)
        let storedDistance = dataManager.getStoredDistance()
        XCTAssertEqual(storedDistance.totalDistance, 1000)
    }

    func testClearData() {
        let location = LocationDataEntity(
            latitude: 37.7749,
            longitude: -122.4194,
            accuracy: 1.0,
            timestamp: 0,
            mockLocation: true
        )
        dataManager.saveLocation(location: location)
        let distance = DistanceEntity(
            totalDistance: 1000,
            todayDistance: 0,
            lastUpdated: ""
        )
        dataManager.saveDistance(distance: distance)
        dataManager.clearData()
        let storedLocations = dataManager.getStoredLocations()
        let storedDistance = dataManager.getStoredDistance()
        XCTAssertEqual(storedLocations.count, 0)
        XCTAssertEqual(storedDistance.totalDistance, 0)
    }
}
