import Flutter
import UIKit

import CoreLocation

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate, CLLocationManagerDelegate {
  var locationManager: CLLocationManager? // Declare a CLLocationManager
  
    override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GeneratedPluginRegistrant.register(with: self)
    
    locationManager = CLLocationManager() // Initialize the CLLocationManager
    locationManager?.delegate = self // この行を追加
    locationManager?.requestWhenInUseAuthorization() // Request When In Use Authorization
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
  func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        switch CLLocationManager.authorizationStatus() {
        case .notDetermined:
            print("When user did not yet determined")
        case .restricted:
            print("Restricted by parental control")
        case .denied:
            print("When user select option Dont't Allow")
        case .authorizedAlways:
            print("When user select option Change to Always Allow")
        case .authorizedWhenInUse:
            print("When user select option Allow While Using App or Allow Once")
            locationManager?.requestAlwaysAuthorization()
        default:
            print("default")
        }
    }

}
