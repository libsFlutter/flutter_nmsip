# Understanding: Project Root

> Entry point for recursive understanding. Children are top-level logical domains.

## Phase: SYNTHESIZING

## Project Overview

**flutter_nmsip** (v2.1.2+108) is a Flutter plugin for SIP (Session Initiation Protocol) communication using the PJSIP library on Android. It provides a comprehensive Dart API for voice/video calling with real-time event streaming and background processing support.

## Identified Domains

> Logical domains discovered. Each becomes a child directory for deeper exploration.

| Domain | Hypothesis | Priority | Status |
|--------|------------|----------|--------|
| endpoint-management | SIP endpoint initialization and configuration | HIGH | DONE |
| account-management | SIP account creation, registration, lifecycle | HIGH | DONE |
| call-management | Call operations (make, answer, control, terminate) | HIGH | DONE |
| event-streaming | Real-time SIP events broadcast to Flutter layer | MEDIUM | DONE |
| native-bridge | Platform channel communication with Android/PJSIP | HIGH | DONE |

## Source Mapping

> Which source paths map to which logical domains

| Source Path | -> Domain |
|-------------|----------|
| lib/flutter_nmsip.dart | endpoint-management, account-management, call-management, native-bridge |
| lib/src/endpoint.dart | endpoint-management |
| lib/src/account.dart | account-management |
| lib/src/account_registration.dart | account-management |
| lib/src/call.dart | call-management |
| android/src/main/kotlin/FlutterSip2Plugin.kt | native-bridge, all domains |
| android/src/main/java/PjActions.java | native-bridge |
| android/src/main/java/PjSipBroadcastReceiver.java | event-streaming |
| android/src/main/kotlin/dto/*.kt | native-bridge |

## Cross-Cutting Concerns

> Things that span multiple domains (may become ADRs)

- **Platform channel architecture**: MethodChannel "flutter_sip2" + EventChannel "flutter_sip2_events"
- **Service-based design**: All operations via Intents to PjSipService for background support
- **Callback pattern**: Sequential callback IDs for async operation tracking
- **Exception handling**: FlutterSip2Exception wraps PlatformException uniformly
- **Data serialization**: JSON serialization via ArgumentUtils for Intent extras
- **Immutable models**: Dart data classes with fromMap/toMap pattern

## Children Spawned

```
All children completed:
- endpoint-management: DONE (sdd-endpoint created)
- account-management: DONE (sdd-account created)
- call-management: DONE (sdd-call created)
- event-streaming: DONE (synthesized)
- native-bridge: DONE (synthesized)
```

## Synthesis

> Updated after all children complete

### Architecture Summary

**flutter_nmsip** uses a service-based architecture with clear separation:

1. **Flutter Layer** (Dart):
   - FlutterSip2 facade with 30+ static methods
   - Immutable data models (Endpoint, Account, AccountRegistration, Call)
   - Real-time duration calculation for calls
   - Exception translation (PlatformException → FlutterSip2Exception)

2. **Platform Bridge** (Kotlin/Java):
   - MethodChannel for commands (25+ methods)
   - EventChannel for real-time events (6 event types)
   - PjSipBroadcastReceiver for callback resolution and event broadcasting
   - Sequential callback ID registration prevents race conditions

3. **Native Service** (PjSipService - not analyzed):
   - Background service for SIP operations
   - PJSIP library integration
   - Intent-based communication enables background execution

### Key Design Decisions

1. **Service-based architecture**: Intents vs direct calls → Background processing support
2. **EventChannel**: Real-time events vs polling → Reactive event streaming
3. **JSON serialization**: Type-safe data transfer across process boundaries
4. **Callback registration**: Sequential IDs for async operation tracking
5. **Immutable models**: Value objects prevent accidental mutation
6. **Real-time duration**: Calculation vs native polling → Reduced overhead

### Flow Coverage

| Domain | Flow | Status |
|--------|------|--------|
| endpoint-management | sdd-endpoint | CREATED (DRAFT) |
| account-management | sdd-account | CREATED (DRAFT) |
| call-management | sdd-call | CREATED (DRAFT) |
| event-streaming | (cross-cutting) | Documented in sdd-endpoint |
| native-bridge | (cross-cutting) | Documented in all SDDs |

### ADR Candidates

| ADR | Topic | Type | Priority |
|-----|-------|------|----------|
| ADR-001 | Service-based architecture (Intents) | enabling | HIGH |
| ADR-002 | EventChannel for real-time events | enabling | HIGH |
| ADR-003 | JSON serialization for Intent extras | enabling | MEDIUM |
| ADR-004 | Callback ID registration pattern | enabling | MEDIUM |
| ADR-005 | Immutable Dart models | enabling | MEDIUM |
| ADR-006 | Real-time duration calculation | enabling | LOW |

## Statistics

- **Dart methods**: 30+ (FlutterSip2 facade)
- **Kotlin methods**: 25+ (platform channel handlers)
- **Event types**: 6 (registration, call, message, connectivity)
- **Data models**: 4 (Endpoint, Account, AccountRegistration, Call)
- **DTOs**: 3 (AccountConfigurationDTO, CallSettingsDTO, SipMessageDTO)

---

*Created by /legacy ENTERING phase | Synthesized after 5 domain analyses*

---

*Created by /legacy ENTERING phase*
