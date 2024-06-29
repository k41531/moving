# Moving

アプリがバックグラウンドまたはキル状態の時でも、位置情報を追跡し、移動距離を計測することができるFlutterプラグインです。

## 機能

- バックグラウンドでの位置情報追跡
- アプリがキル状態でも継続的に動作
- 移動距離の計算と保存
- バッテリー消費を最適化
- プライバシーに配慮した設計

## インストール

`pubspec.yaml` ファイルに以下を追加してください：

```yaml
dependencies:
  moving: ^1.0.0
```

そして、以下のコマンドを実行してください：

```
$ flutter pub get
```

## セットアップ

### iOS

1. `ios/Runner/Info.plist` に以下を追加してください：

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>このアプリは、バックグラウンドで位置情報を使用して移動距離を計算します。</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>このアプリは、バックグラウンドで位置情報を使用して移動距離を計算します。</string>
<key>UIBackgroundModes</key>
<array>
    <string>location</string>
</array>
```

2. Xcode でプロジェクトを開き、Capabilities タブで "Background Modes" を有効にし、"Location updates" を選択してください。

## 使用方法

```dart
import 'package:moving/background_location_tracker.dart';

// 追跡を開始
await BackgroundLocationTracker.startTracking();

// 現在の距離を取得
double distance = await BackgroundLocationTracker.getCurrentDistance();

// 距離をリセット
await BackgroundLocationTracker.resetDistance();

// 追跡を停止
await BackgroundLocationTracker.stopTracking();

// リアルタイムの位置情報を取得
BackgroundLocationTracker.getLocationStream().listen((Map<String, double> location) {
  print('Latitude: ${location['latitude']}, Longitude: ${location['longitude']}');
});
```

## 注意事項

- このプラグインは、ユーザーのプライバシーに大きな影響を与える可能性があります。アプリの利用規約とプライバシーポリシーに、位置情報の使用について明確に記載してください。
- バックグラウンドでの位置情報の使用は、バッテリーを著しく消費する可能性があります。必要な時だけ追跡を有効にすることをお勧めします。
- App Store に提出する際は、バックグラウンドでの位置情報使用の正当な理由を説明する必要があります。

## トラブルシューティング

位置情報が更新されない場合：
- デバイスの位置情報サービスが有効になっているか確認してください。
- アプリに適切な権限が付与されているか確認してください。
- 省電力モードが有効になっていないか確認してください。

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

バグの報告や機能の提案は、Githubの[Issues](https://github.com/yourusername/flutter_background_location_tracker/issues)までお願いします。
プルリクエストも歓迎します。

## サポート

質問やサポートが必要な場合は、[Discussions](https://github.com/yourusername/flutter_background_location_tracker/discussions)をご利用ください。