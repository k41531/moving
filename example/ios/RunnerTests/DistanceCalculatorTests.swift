import XCTest
import CoreLocation

@testable import moving

class DistanceCalculatorTests: XCTestCase {

    var calculator: DistanceCalculator!

    override func setUp() {
        super.setUp()
        calculator = DistanceCalculator()
    }

    override func tearDown() {
        calculator = nil
        super.tearDown()
    }

    func testCalculateDistance() {
        let location1 = (latitude: 35.6895, longitude: 139.6917) // 東京
        let location2 = (latitude: 34.6937, longitude: 135.5023) // 大阪

        let distance = calculator.calculateDistance(from: location1, to: location2)

        let expectedDistance: Double = 397183080 // メートル単位
        let tolerance: Double = 10 // 1メートル

        XCTAssertEqual(distance, expectedDistance, accuracy: tolerance, "計算された距離が期待値と一致しません")
    }

    func testCalculateDistanceWithSameLocation() {
        let location = (latitude: 35.6895, longitude: 139.6917)

        let distance = calculator.calculateDistance(from: location, to: location)

        XCTAssertEqual(distance, 0, "同じ位置の場合、距離は0になるべきです")
    }

    func testCalculateDistanceWithInvalidLocation() {
        let validLocation = (latitude: 35.6895, longitude: 139.6917)
        let invalidLocation = (latitude: 91.0, longitude: 181.0) // 無効な緯度経度

        let distance = calculator.calculateDistance(from: validLocation, to: invalidLocation)

        XCTAssertEqual(distance, -1, "無効な位置の場合、距離は-1を返すべきです")
    }
}
