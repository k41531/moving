import CoreLocation

enum DistanceUnit {
    case kilometers
    case meters
}

class DistanceCalculator {
    
    func calculateDistance(from pointA: (Double, Double), to pointB: (Double, Double), unit: DistanceUnit = .meters) -> Double {
        guard isValidLocation(pointA) && isValidLocation(pointB) else {
            return -1
        }
        
        let locationA = CLLocation(latitude: pointA.0, longitude: pointA.1)
        let locationB = CLLocation(latitude: pointB.0, longitude: pointB.1)
        let distance = locationA.distance(from: locationB)
        
        return convertDistance(distance, toUnit: unit)
    }
    
    private func isValidLocation(_ point: (Double, Double)) -> Bool {
        if point.0 < -90 || point.0 > 90 {
            return false
        }
        if point.1 < -180 || point.1 > 180 {
            return false
        }
        return true
    }
    
    private func convertDistance(_ distance: Double, toUnit unit:DistanceUnit) -> Double {
        switch unit {
        case .kilometers:
            return distance
        case .meters:
            return distance * 1000
        }
    }
}
