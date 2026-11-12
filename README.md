# DACN — Decentralised Autonomous Communication Network

A secure, infrastructure-independent mobile client for offline peer-to-peer messaging, encrypted large file distribution, REST API telemetry, and compass radar navigation. DACN operates without cellular coverage, internet gateways, or centralized authentication servers.

---

## Technical Highlights & Key Features

### 1. Persistent Ed25519 Identity Module (`DACNIdentityManager`)
- **Keypair Generation**: BouncyCastle Ed25519 keypair generated on first launch.
- **Hardware Security**: Keys stored securely via `EncryptedSharedPreferences` (Android KeyStore master key + AES-256-GCM).
- **Cryptographic Peer ID**: `peerID` derived as a SHA-256 hash of public key bytes (64-character lowercase hex string).
- **Display Name**: User display name persisted across app restarts.

### 2. Encrypted File Transfer Engine (`DACNFileTransferManager`)
- **File Capacity**: Supports files up to **5MB**.
- **Key Exchange**: Asymmetric **X25519 ECDH** key agreement with ephemeral sender keypairs.
- **Symmetric Encryption**: **AES-256-GCM** (12-byte IV + 128-bit authentication tag).
- **BLE Mesh Fragmentation**: Fragments payloads into **512-byte BLE-compatible chunks**.
- **Integrity Guarantee**: Receiver validates full-file **SHA-256 hash** before saving.

### 3. Local Web Dashboard REST API (`DACNDashboardServer`)
- **Embedded Web Server**: Runs `NanoHTTPD` locally on port **8080**.
- **`GET /api/identity`**: Exposes JSON payload containing `publicKey`, `peerID`, and `displayName`.
- **`GET /api/mesh`**: Exposes JSON payload with connected peers, active transport status, hop counts, and delivery stats.

### 4. 100% Offline Map & Compass Radar Navigation (`DACNOfflineMapEngine`)
- **Offline Math**: Calculates Haversine distance, initial bearing, and 8-point compass heading (N, NE, E, SE, S, SW, W, NW) offline without internet.
- **Compose Radar UI**: Interactive concentric range rings, center beacon, and relative peer position.
- **Offline Map App Launch**: Triggers `geo:` intents to launch pre-downloaded offline vector maps (**OsmAnd**, **Organic Maps**, **MAPS.ME**).

### 5. Multilingual Localization (English & Swahili)
- Full Swahili translations in `values-sw/strings.xml` and locale support in `locales_config.xml`.

---

## Android Stack & Architecture

- **Language & UI**: Kotlin, Jetpack Compose (Material 3), MVVM Architecture
- **Cryptography**: BouncyCastle (`bcprov-jdk18on`), AndroidX Security Crypto (`security-crypto`)
- **BLE Transports**: `BluetoothMeshService`, `UnifiedMeshService`, `MeshForegroundService`
- **Telemetry**: `NanoHTTPD` on port 8080

---

## Building & Installation

Requires Android Studio with JDK 21 and Android SDK (API 26+).

```bash
git clone https://github.com/Truepele001/DACN.git
cd DACN
./gradlew assembleDebug
```

Install on connected Android device:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Testing

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Lint check
./gradlew lintDebug
```

---

## License

Released under GPL-3.0 License.
