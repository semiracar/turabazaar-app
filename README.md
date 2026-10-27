# Turabazaar App

Offizielle mobile App von [turabazaar.com](https://turabazaar.com) – dem Kultur-Marktplatz
für verifizierte Händler, Hersteller und Künstler aus Mesopotamien und der ganzen Welt.

## Struktur

| Ordner | Inhalt |
|---|---|
| `app-android/` | Android-App (Kotlin, WebView + Firebase Cloud Messaging) – Paket `com.turabazaar.app` |
| `app-ios/` | iOS-App (SwiftUI WebView, via [xcodegen](https://github.com/yonsm/XcodeGen)) – Bundle-ID `com.turabazaar.app` |

## Android bauen

```bash
cd app-android
./gradlew assembleRelease   # benötigt keystore/ (lokal, nicht im Repo)
```

## iOS bauen

```bash
cd app-ios
brew install xcodegen
xcodegen
xcodebuild -scheme Turabazaar -destination 'generic/platform=iOS' build
```

## CI

- `.github/workflows/android-build.yml` – baut signiertes APK/AAB (Secrets: Keystore)
- `.github/workflows/ios-build.yml` – baut die iOS-App auf macOS

## App Store

- Apple App Store: App-ID `6827757159`, Team-ID `W6Q8QMSJH5`
- Google Play: Konto eingerichtet, Upload nach Identitätsfreigabe

© 2026 Semir Acar – Turabazaar
