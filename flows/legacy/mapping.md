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
| lib/flutter_nmsip.dart (start, config methods) | sdd-endpoint | SDD | CREATED | DRAFT | Endpoint initialization, configuration |
| android/src/main/kotlin/FlutterSip2Plugin.kt | sdd-endpoint | SDD | CREATED | DRAFT | Platform channel handler |
| android/src/main/java/PjActions.java | sdd-endpoint | SDD | CREATED | DRAFT | Intent factory |
| android/src/main/java/PjSipBroadcastReceiver.java | sdd-endpoint | SDD | CREATED | DRAFT | Event broadcasting |
| lib/src/endpoint.dart | sdd-endpoint | SDD | CREATED | DRAFT | Endpoint data model |
| lib/src/account.dart | - | - | PENDING | - | Account management |
| lib/src/account_registration.dart | - | - | PENDING | - | Registration status |
| lib/src/call.dart | - | - | PENDING | - | Call model |

## ADR Mapping

| Code Pattern | ADR | Type | Status |
|--------------|-----|------|--------|
| Service-based architecture (Intents) | ADR-001 | enabling | PENDING | Background processing support |
| EventChannel for real-time events | ADR-002 | enabling | PENDING | Reactive event streaming |
| JSON serialization for Intent extras | ADR-003 | enabling | PENDING | Type-safe data transfer |
| Callback ID registration pattern | ADR-004 | enabling | PENDING | Async operation tracking |

## Unmapped (needs manual review)

| Code Path | Reason |
|-----------|--------|
| android/src/test/ | Test code - may become TDD |
| example/ | Example app - separate project |

---

*Auto-generated. Update as analysis progresses.*
