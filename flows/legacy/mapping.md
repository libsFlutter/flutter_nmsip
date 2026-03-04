# Code to Flow Mapping

## Overview

Maps analyzed code modules to generated flows.

## Flow Type Detection Rules

| Indicator | Flow Type |
|-----------|-----------|
| `*.test.*`, `*.spec.*`, `__tests__/` | TDD |
| `components/`, `*.tsx`, `*.vue`, `templates/` | VDD |
| `README.md`, public exports, API docs | DDD |
| Internal logic, no UI, no public API | SDD |

## Mapping Table

| Code Path | Flow | Type | Action | Status | Notes |
|-----------|------|------|--------|--------|-------|
| lib/flutter_nmsip.dart (start, config) | sdd-endpoint | SDD | CREATED | DRAFT | Endpoint initialization |
| lib/src/endpoint.dart | sdd-endpoint | SDD | CREATED | DRAFT | Endpoint data model |
| lib/flutter_nmsip.dart (createAccount, register, delete) | sdd-account | SDD | CREATED | DRAFT | Account operations |
| lib/src/account.dart | sdd-account | SDD | CREATED | DRAFT | Account model |
| lib/src/account_registration.dart | sdd-account | SDD | CREATED | DRAFT | Registration status |
| lib/flutter_nmsip.dart (call operations) | sdd-call | SDD | CREATED | DRAFT | Call operations |
| lib/src/call.dart | sdd-call | SDD | CREATED | DRAFT | Call model |
| android/src/main/kotlin/FlutterSip2Plugin.kt | sdd-endpoint | SDD | CREATED | DRAFT | Platform channel handler |
| android/src/main/java/PjActions.java | sdd-endpoint | SDD | CREATED | DRAFT | Intent factory |
| android/src/main/java/PjSipBroadcastReceiver.java | sdd-endpoint | SDD | CREATED | DRAFT | Event broadcasting |
| android/src/main/kotlin/dto/AccountConfigurationDTO.kt | sdd-account | SDD | CREATED | DRAFT | Account config DTO |
| android/src/main/kotlin/dto/CallSettingsDTO.kt | sdd-call | SDD | CREATED | DRAFT | Call settings DTO |
| android/src/test/ | tdd-plugin | TDD | PENDING | - | Plugin unit tests |
| example/ | - | - | DEFERRED | - | Example app (separate) |

## ADR Mapping

| Code Pattern | ADR | Type | Status |
|--------------|-----|------|--------|
| Service-based architecture (Intents) | ADR-001 | enabling | PENDING | Background processing |
| EventChannel for real-time events | ADR-002 | enabling | PENDING | Reactive streaming |
| JSON serialization for Intent extras | ADR-003 | enabling | PENDING | Type-safe transfer |
| Callback ID registration pattern | ADR-004 | enabling | PENDING | Async tracking |
| Immutable Dart models | ADR-005 | enabling | PENDING | Value objects |
| Real-time duration calculation | ADR-006 | enabling | PENDING | No native polling |

## Unmapped (needs manual review)

| Code Path | Reason |
|-----------|--------|
| android/src/test/kotlin/FlutterSip2PluginTest.kt | Test code - may become TDD |
| example/ | Example app - separate project |

---

*Auto-generated. Update as analysis progresses.*
