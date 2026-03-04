# Understanding: Project Root

> Entry point for recursive understanding. Children are top-level logical domains.

## Phase: EXPLORING

## Project Overview

**flutter_nmsip** (v2.1.2+108) is a Flutter plugin for SIP (Session Initiation Protocol) communication using the PJSIP library on Android. It provides a Dart API for voice/video calling functionality with real-time event streaming.

## Identified Domains

> Logical domains discovered. Each becomes a child directory for deeper exploration.

| Domain | Hypothesis | Priority | Status |
|--------|------------|----------|--------|
| endpoint-management | SIP endpoint initialization and configuration | HIGH | PENDING |
| account-management | SIP account creation, registration, lifecycle | HIGH | PENDING |
| call-management | Call operations (make, answer, control, terminate) | HIGH | PENDING |
| event-streaming | Real-time SIP events broadcast to Flutter layer | MEDIUM | PENDING |
| native-bridge | Platform channel communication with Android/PJSIP | HIGH | PENDING |

## Source Mapping

> Which source paths map to which logical domains

| Source Path | -> Domain |
|-------------|----------|
| lib/flutter_nmsip.dart | endpoint-management, native-bridge |
| lib/src/endpoint.dart | endpoint-management |
| lib/src/account.dart | account-management |
| lib/src/account_registration.dart | account-management |
| lib/src/call.dart | call-management |
| android/src/ | native-bridge |

## Cross-Cutting Concerns

> Things that span multiple domains (may become ADRs)

- **Platform channel architecture**: MethodChannel + EventChannel for native communication
- **Exception handling**: FlutterSip2Exception wraps PlatformException uniformly
- **Data serialization**: All models use fromMap/toMap pattern
- **State tracking**: Call duration tracking with _constructionTime

## Validated Understanding

**Confirmed Architecture:**
- Flutter plugin with Android native implementation
- MethodChannel for commands, EventChannel for real-time events
- 4 core Dart classes: FlutterSip2 (facade), Endpoint, Account, Call
- Account includes registration status (AccountRegistration)
- Call has extensive state tracking and duration calculation
- All models are immutable with fromMap/toMap serialization

**Key Patterns:**
- Exception translation: PlatformException → FlutterSip2Exception
- Event stream is lazy-initialized (singleton pattern)
- Call duration tracking uses _constructionTime for accurate elapsed time
- URI parsing for remote/local number/name extraction

## Children Spawned

> Deeper concepts to explore during SPAWNING phase

| Child | Hypothesis | Status |
|-------|------------|--------|
| endpoint-management | SIP endpoint initialization, connectivity, settings | PENDING |
| account-management | Account creation, registration lifecycle, credentials | PENDING |
| call-management | Call operations, state machine, media control | PENDING |
| event-streaming | Event types, broadcasting, subscription model | PENDING |
| native-bridge | Platform channels, Android/PJSIP integration | PENDING |

## Synthesis

> Updated after all children complete

[pending children completion]

---

*Created by /legacy ENTERING phase*
