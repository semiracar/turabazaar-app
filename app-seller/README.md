# Turabazaar Android-App

Native Android-Hülle (WebView) für turabazaar.com mit Firebase-Push (FCM).

## Struktur
- `app/` – Kotlin-App (Paket `com.turabazaar.app`)
- `app/google-services.json` – Firebase-Konfiguration (Projekt turabazaar-204d8)
- `branding/` – Logo & Icons (Quelle: Live-Seite)
- `keystore/` – Upload-Key (lokal bzw. CI-Artefakt)

## Build
Lokal nicht nötig – GitHub Actions baut automatisch bei jedem Push:
- `app-release.aab` → Upload bei Google Play
- `app-release.apk` → Direktinstallation

## Wichtig
- Der Upload-Keystore muss **immer derselbe** bleiben, sonst kann Google Play
  keine Updates akzeptieren. Einmal erzeugten Keystore sicher aufbewahren
  (CI erzeugt ihn beim ersten Lauf als Artefakt „upload-keystore").
- Push-Token-Fluss: App → `window.TuraPush.setToken()` → Server speichert Token
  pro Nutzer (bereits serverseitig vorbereitet, `src/lib/Fcm.php`).
