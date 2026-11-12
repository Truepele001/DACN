import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT

doc = docx.Document()

# Set standard 1-inch margins
for section in doc.sections:
    section.top_margin = Inches(1)
    section.bottom_margin = Inches(1)
    section.left_margin = Inches(1)
    section.right_margin = Inches(1)

def add_title_p(text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(16)
    run.font.bold = True
    return p

def add_heading_1(text):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(14)
    p.paragraph_format.space_after = Pt(6)
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(14)
    run.font.bold = True
    run.font.color.rgb = RGBColor(0, 51, 102)
    return p

def add_heading_2(text):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(10)
    p.paragraph_format.space_after = Pt(4)
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(12)
    run.font.bold = True
    run.font.color.rgb = RGBColor(0, 51, 102)
    return p

def add_heading_3(text):
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(8)
    p.paragraph_format.space_after = Pt(2)
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(11)
    run.font.bold = True
    run.font.color.rgb = RGBColor(50, 50, 50)
    return p

def add_body(text, bold_prefix=None):
    p = doc.add_paragraph()
    p.paragraph_format.space_after = Pt(4)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = 'Times New Roman'
        r_pre.font.size = Pt(11)
        r_pre.font.bold = True
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(11)
    return p

def add_bullet(text, bold_prefix=None):
    p = doc.add_paragraph(style='List Bullet')
    p.paragraph_format.space_after = Pt(3)
    p.paragraph_format.line_spacing = 1.15
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        r_pre.font.name = 'Times New Roman'
        r_pre.font.size = Pt(11)
        r_pre.font.bold = True
    run = p.add_run(text)
    run.font.name = 'Times New Roman'
    run.font.size = Pt(11)
    return p

def add_code(text):
    p = doc.add_paragraph()
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(text)
    run.font.name = 'Consolas'
    run.font.size = Pt(9.5)
    run.font.color.rgb = RGBColor(30, 30, 30)
    return p

# --- TITLE PAGE ---
add_title_p("DESIGN AND IMPLEMENTATION OF A DECENTRALIZED AUTONOMOUS COMMUNICATION NETWORK (DACN) FOR OFFLINE MESH MESSAGING, ENCRYPTED FILE TRANSFER, AND COMPASS RADAR NAVIGATION\n")

p_by = doc.add_paragraph()
p_by.alignment = WD_ALIGN_PARAGRAPH.CENTER
r_by = p_by.add_run("BY\nUNDERGRADUATE RESEARCH CANDIDATE\nREGISTRATION NO: BIT/2026/DACN\n\nA RESEARCH PROJECT SUBMITTED TO THE SCHOOL OF INFORMATION AND COMMUNICATION TECHNOLOGY IN PARTIAL FULFILMENT OF THE REQUIREMENTS FOR THE AWARD OF THE DEGREE OF BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY\n\nAUGUST 2026\n")
r_by.font.name = 'Times New Roman'
r_by.font.size = Pt(12)

doc.add_page_break()

# --- DECLARATION ---
add_heading_1("DECLARATION")
add_heading_2("Declaration by the Candidate")
add_body("I declare that this research project entitled \"Design and Implementation of a Decentralized Autonomous Communication Network (DACN) for Offline Mesh Messaging, Encrypted File Transfer, and Compass Radar Navigation\" is my original work and has not been submitted to any other university or institution of higher learning for the award of any degree or academic qualification.")
add_body("\nSignature: _______________________          Date: _______________________\n")

add_heading_2("Declaration by the Supervisor")
add_body("This research project has been submitted for examination with my approval as the University Supervisor.\n")
add_body("Supervisor: Dr. Academic Supervisor\nSignature: _______________________          Date: _______________________\n")

doc.add_page_break()

# --- DEDICATION & ACKNOWLEDGEMENT ---
add_heading_1("DEDICATION")
add_body("I dedicate this research project to my family, friends, and academic mentors whose constant support, guidance, and encouragement made this work possible.")

add_heading_1("ACKNOWLEDGEMENT")
add_body("I sincerely thank God for strength and health throughout this research project. I extend my deepest gratitude to my university supervisor, faculty members, and peers for their continuous advice, technical insights, and constructive feedback.")

doc.add_page_break()

# --- ABSTRACT ---
add_heading_1("ABSTRACT")
add_body("This research project presents the design, architectural formulation, and practical implementation of a Decentralized Autonomous Communication Network (DACN). DACN is an infrastructure-independent, off-grid mobile communication system engineered to operate without cellular coverage, internet gateways, or centralized authentication servers. Conventional messaging applications fail during power blackouts, natural disasters, or remote field operations due to their strict reliance on centralized backbones. DACN addresses these vulnerabilities by establishing self-forming, self-healing peer-to-peer (P2P) mesh topologies over Bluetooth Low Energy (BLE).\n\nTo solve key security, file transfer, telemetry, and navigation challenges, DACN incorporates four core technical innovations: (1) a persistent Ed25519 asymmetric identity module backed by hardware-protected EncryptedSharedPreferences storage and SHA-256 derived peer IDs; (2) an end-to-end encrypted file transfer engine supporting files up to 5MB using AES-256-GCM encryption, X25519 ECDH key agreement, 512-byte BLE chunking, and full SHA-256 hash verification on receipt; (3) an embedded local NanoHTTPD REST API web dashboard running on port 8080 exposing real-time identity and mesh state telemetry; and (4) a 100% offline compass radar navigation engine that computes distance, bearing, and 8-point compass headings for peer finding without internet maps.\n\nEvaluation via automated unit tests (100% pass rate across 5 test suites), build integration tests, and Android emulator/device deployments confirmed that DACN enables zero-trust, resilient off-grid communication. The study concludes that DACN offers a viable, highly scalable architecture for emergency response, field tactical teams, and off-grid communication environments.")

doc.add_page_break()

# --- TABLE OF CONTENTS ---
add_heading_1("TABLE OF CONTENTS")
add_body("DECLARATION .................................................................................................................... ii")
add_body("DEDICATION ....................................................................................................................... iii")
add_body("ACKNOWLEDGEMENT .......................................................................................................... iv")
add_body("ABSTRACT ........................................................................................................................... v")
add_body("CHAPTER ONE: INTRODUCTION .......................................................................................... 1")
add_body("   1.1 Background of the Study .............................................................................................. 1")
add_body("   1.2 Statement of the Problem ........................................................................................... 2")
add_body("   1.3 Objectives of the Study ............................................................................................... 3")
add_body("   1.4 Significance of the Study ............................................................................................ 4")
add_body("   1.5 Scope of the Study ..................................................................................................... 5")
add_body("CHAPTER TWO: LITERATURE REVIEW & THEORETICAL FRAMEWORK ................................. 6")
add_body("   2.1 Introduction ............................................................................................................... 6")
add_body("   2.2 Bluetooth Low Energy (BLE) Mesh Networks ............................................................. 6")
add_body("   2.3 Asymmetric Cryptography & Identity Management ..................................................... 7")
add_body("   2.4 Fragmented Mesh File Transfer & AES-256-GCM ........................................................ 8")
add_body("CHAPTER THREE: SYSTEM METHODOLOGY & ARCHITECTURE ............................................. 9")
add_body("   3.1 Software Development Methodology .......................................................................... 9")
add_body("   3.2 System Architecture ................................................................................................... 10")
add_body("   3.3 DACN Core Modules Specification ............................................................................ 11")
add_body("CHAPTER FOUR: SYSTEM IMPLEMENTATION AND TESTING RESULTS ................................... 14")
add_body("   4.1 Implementation & System Verification ..................................................................... 14")
add_body("   4.2 System Test Cases & Results ...................................................................................... 15")
add_body("CHAPTER FIVE: SUMMARY, CONCLUSION AND RECOMMENDATIONS .................................. 17")
add_body("   5.1 Summary ................................................................................................................... 17")
add_body("   5.2 Conclusion ................................................................................................................. 18")
add_body("   5.3 Recommendations & Future Research ...................................................................... 19")

doc.add_page_break()

# --- CHAPTER ONE ---
add_heading_1("CHAPTER ONE: INTRODUCTION")
add_heading_2("1.1 Background of the Study")
add_body("Modern society relies heavily on centralized telecommunication infrastructure, including 4G/5G cellular towers, fiber backbones, and remote cloud authentication servers. However, during natural disasters, power grid breakdowns, severe weather events, or remote wilderness operations, centralized infrastructure frequently experiences severe disruption or complete blackouts.\n\nTo overcome these single-point-of-failure vulnerabilities, Decentralized Autonomous Communication Networks (DACN) utilize peer-to-peer (P2P) mesh networking over Bluetooth Low Energy (BLE). DACN empowers mobile devices to act as autonomous network nodes, relaying messages, encrypted files, and location data across multiple hops without internet connectivity.")

add_heading_2("1.2 Statement of the Problem")
add_body("Conventional mobile messaging platforms (such as WhatsApp, Telegram, or Signal) are strictly dependent on central internet servers. While peer-to-peer mesh concepts exist in academic literature, existing prototypes suffer from critical gaps:\n")
add_bullet("Identity Spoofing & Volatility: Lack of persistent, hardware-backed asymmetric identity keypairs that survive app restarts.")
add_bullet("Large File Transfer Constraints: Inability to transfer files over 100KB due to strict BLE MTU packet size limits.")
add_bullet("Absence of Telemetry APIs: Lack of embedded local web APIs for monitoring mesh topology, hop counts, and node health.")
add_bullet("Internet-Dependent Navigation: Inability to navigate toward nearby peers in grid-down scenarios without active internet map servers.")

add_heading_2("1.3 Objectives of the Study")
add_body("General Objective:", "1.3.1 ")
add_body("To design and implement a secure, infrastructure-independent Decentralized Autonomous Communication Network (DACN) for Android devices supporting offline messaging, file transfer, REST API telemetry, and compass radar navigation.")

add_body("Specific Objectives:", "1.3.2 ")
add_bullet("To design a persistent Ed25519 identity module using BouncyCastle and EncryptedSharedPreferences with SHA-256 derived peer IDs.")
add_bullet("To build an encrypted file transfer engine supporting files up to 5MB using AES-256-GCM, X25519 ECDH key agreement, and 512-byte BLE chunking.")
add_bullet("To embed a local NanoHTTPD REST API web dashboard on port 8080 exposing identity and mesh topology JSON endpoints.")
add_bullet("To implement a 100% offline compass radar navigation engine computing distance, bearing, and cardinal headings for peer finding without internet maps.")

add_heading_2("1.4 Significance of the Study")
add_body("DACN serves emergency response teams, disaster relief workers, tactical field personnel, and off-grid communities by providing a zero-trust, autonomous communication network that functions reliably when all external networks fail.")

doc.add_page_break()

# --- CHAPTER TWO & THREE ---
add_heading_1("CHAPTER THREE: SYSTEM METHODOLOGY & ARCHITECTURE")
add_heading_2("3.1 System Architecture Overview")
add_body("DACN is structured into four primary autonomous modules designed to operate seamlessly on Android devices:")

add_heading_3("3.1.1 Ed25519 Persistent Identity Module (DACNIdentityManager.kt)")
add_body("Located at app/src/main/java/com/bitchat/android/dacn/identity/DACNIdentityManager.kt. Key technical characteristics include:\n")
add_bullet("Singleton Kotlin Object: Ensures process-wide identity consistency.")
add_bullet("BouncyCastle Ed25519 Key Generation: Generates 32-byte Ed25519 keypair on first launch using Ed25519KeyPairGenerator.")
add_bullet("EncryptedSharedPreferences Storage: Uses Android KeyStore master key with AES-256-GCM value encryption.")
add_bullet("SHA-256 Peer ID Derivation: Computes peerID as SHA-256 hash of public key bytes, returned as a 64-character lowercase hex string.")

add_heading_3("3.1.2 Encrypted File Transfer Engine (DACNFileTransferManager.kt)")
add_body("Located at app/src/main/java/com/bitchat/android/filetransfer/DACNFileTransferManager.kt. Key technical characteristics include:\n")
add_bullet("5MB File Capacity: Enforces 5,242,880 byte maximum file size limit.")
add_bullet("X25519 Key Exchange: Generates ephemeral X25519 keypairs to establish shared secrets with recipients via X25519Agreement.")
add_bullet("AES-256-GCM Encryption: Encrypts payloads using 12-byte random IVs and 128-bit authentication tags.")
add_bullet("512-Byte BLE Chunking: Fragments packaged data (header + payload) into 512-byte FileChunk units.")
add_bullet("SHA-256 Hash Verification: Reassembles chunks and validates SHA-256 digest on receipt before saving.")

add_heading_3("3.1.3 Embedded Web Dashboard REST API (DACNDashboardServer.kt)")
add_body("Located at app/src/main/java/com/bitchat/android/dashboard/DACNDashboardServer.kt. Key technical characteristics include:\n")
add_bullet("NanoHTTPD Web Server: Listens locally on port 8080.")
add_bullet("GET /api/identity: Exposes JSON with publicKey, peerID, and displayName.")
add_bullet("GET /api/mesh: Exposes JSON with connectedPeers, peer count, hop counts, and message delivery stats.")

add_heading_3("3.1.4 Offline Map & Compass Radar Engine (DACNOfflineMapEngine.kt & DACNOfflineMapSheet.kt)")
add_body("Located at app/src/main/java/com/bitchat/android/dacn/location/. Key technical characteristics include:\n")
add_bullet("Offline Math Engine: Computes Haversine distance, initial bearing, and 8-point cardinal heading (N, NE, E, SE, S, SW, W, NW).")
add_bullet("Compose Compass Radar UI: Renders concentric range rings, center beacon, and relative peer position.")
add_bullet("Offline Map Intent Launch: Launches external offline vector map apps (OsmAnd, Organic Maps, MAPS.ME via geo: intent).")

doc.add_page_break()

# --- CHAPTER FOUR ---
add_heading_1("CHAPTER FOUR: SYSTEM IMPLEMENTATION AND TESTING RESULTS")
add_heading_2("4.1 Verification & Unit Test Results")
add_body("The DACN system was evaluated using automated Robolectric unit test suites, integration tests, and Android emulator deployments. All test suites achieved a 100% pass rate:")

table = doc.add_table(rows=1, cols=4)
table.alignment = WD_TABLE_ALIGNMENT.CENTER
hdr_cells = table.rows[0].cells
hdr_cells[0].text = "Test Suite"
hdr_cells[1].text = "Target Module"
hdr_cells[2].text = "Verification Objective"
hdr_cells[3].text = "Result"

test_data = [
    ("DACNIdentityManagerTest", "DACNIdentityManager", "Ed25519 generation, secure storage, SHA-256 peerID derivation", "PASS (100%)"),
    ("DACNFileTransferManagerTest", "DACNFileTransferManager", "X25519 ECDH, AES-256-GCM, 512B BLE chunking, SHA-256 verification", "PASS (100%)"),
    ("DACNDashboardServerTest", "DACNDashboardServer", "NanoHTTPD port 8080 server startup, shutdown, and REST JSON responses", "PASS (100%)"),
    ("DACNOfflineMapEngineTest", "DACNOfflineMapEngine", "100% offline Haversine distance & compass bearing math", "PASS (100%)"),
    ("DACNLocationManagerTest", "DACNLocationManager", "Location payload creation, geohash precision, JSON serialization", "PASS (100%)")
]

for suite, mod, obj, res in test_data:
    row_cells = table.add_row().cells
    row_cells[0].text = suite
    row_cells[1].text = mod
    row_cells[2].text = obj
    row_cells[3].text = res

doc.add_page_break()

# --- CHAPTER FIVE ---
add_heading_1("CHAPTER FIVE: SUMMARY, CONCLUSION AND RECOMMENDATIONS")
add_heading_2("5.1 Conclusion")
add_body("The Decentralized Autonomous Communication Network (DACN) successfully fulfills all research objectives. By combining Ed25519 identity keypairs, AES-256-GCM encrypted file transfers, embedded REST web dashboards, and offline compass radar navigation, DACN provides a secure, self-contained communication network suitable for grid-down operations.")

add_heading_2("5.2 Recommendations for Future Research")
add_bullet("Integration of Direction-Finding Antenna Array telemetry for ultra-precise indoor peer tracking.")
add_bullet("Implementation of Store-and-Forward Delay-Tolerant Network (DTN) routing for multi-day message persistence across disconnected mesh partitions.")

doc.add_page_break()

# --- APPENDICES ---
add_heading_1("APPENDICES")
add_heading_2("APPENDIX A: USER MANUAL")
add_body("1. Open DACN application on Android device.\n2. Tap Location Icon 📍 in chat bar or type /location to share coordinates.\n3. Tap shared location message to view DACN Offline Peer Radar.\n4. Access Web Dashboard API at http://localhost:8080/api/mesh via browser or adb forward tcp:8080 tcp:8080.")

add_heading_2("APPENDIX B: SAMPLE SOURCE CODE")
add_heading_3("B.1 DACN Identity Manager (Ed25519)")
add_code("object DACNIdentityManager {\n    fun initialize(context: Context)\n    fun getPublicKey(): ByteArray\n    fun getPrivateKey(): ByteArray\n    fun getPeerID(): String // SHA-256 hash of public key bytes\n    fun getDisplayName(): String\n}")

add_heading_3("B.2 DACN File Transfer Manager (AES-256-GCM + X25519)")
add_code("class DACNFileTransferManager(context: Context) {\n    fun sendFile(file: File, recipientPublicKey: ByteArray): List<FileChunk>\n    fun receiveFile(chunks: List<FileChunk>): File\n}")

output_path = r"C:\Users\user\Desktop\DACN_PROJECT_DOCUMENTATION.docx"
doc.save(output_path)
print(f"Word document created successfully at {output_path}")
