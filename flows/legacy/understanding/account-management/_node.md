# Understanding: Account Management

> SIP account creation, registration lifecycle, and credential management.

## Phase: SYNTHESIZING

## Hypothesis

Account management handles:
- Creating SIP accounts with credentials (username, domain, password)
- Account registration/renewal with SIP server
- Registration status tracking (AccountRegistration)
- Account deletion and cleanup

## Sources

- lib/flutter_nmsip.dart - createAccount, registerAccount, deleteAccount
- lib/src/account.dart - Account data class
- lib/src/account_registration.dart - Registration status
- android/src/main/kotlin/FlutterSip2Plugin.kt - Native method handlers
- android/src/main/java/PjActions.java - Account intents
- android/src/main/kotlin/org/tele/flutter_sip2/dto/AccountConfigurationDTO.kt - Account config

## Validated Understanding

**Confirmed Architecture:**

1. **Account Creation Flow**:
   - Dart: `FlutterSip2.createAccount(Map<String, dynamic>)` → Account
   - Kotlin: MethodChannel handler registers callback, creates Intent
   - PjActions: `createAccountCreateIntent(callbackId, configuration, context)`
   - Configuration serialized via AccountConfigurationDTO.toJson()
   - Service responds with EVENT_ACCOUNT_CREATED
   - Account parsed from JSON response

2. **Account Configuration Fields**:
   - **Required**: name, username, domain, password
   - **Optional**: proxy, transport, regServer, regTimeout (default 3600), regHeaders, regContactParams
   - Transport defaults to TCP if not specified
   - regHeaders allows custom SIP headers (e.g., X-Custom-Header)

3. **Registration Flow**:
   - Dart: `FlutterSip2.registerAccount(account, renew: bool)`
   - Parameters: accountId (int), renew (boolean, default true)
   - Intent action: ACTION_REGISTER_ACCOUNT
   - Service responds with EVENT_REGISTRATION_CHANGED

4. **Registration Status Model** (AccountRegistration):
   - status: bool (registered/unregistered)
   - code: Int? (SIP status code, e.g., 200 OK)
   - reason: String? (error message)
   - expiration: Int? (seconds until expiry)
   - retryAfter: Int? (seconds before retry)

5. **Account Deletion**:
   - Dart: `FlutterSip2.deleteAccount(account)`
   - Passes accountId to service
   - Intent action: ACTION_DELETE_ACCOUNT
   - Cleans up account resources

6. **Account Model** (Dart):
   - Immutable data class with fromMap/toMap
   - Equality by id only
   - Includes nested AccountRegistration object
   - toString() for debugging

## Children Identified

> Deeper concepts - analyzed inline (no separate nodes needed)

| Child | Status | Notes |
|-------|--------|-------|
| account-creation | ANALYZED | Configuration DTO with JSON serialization |
| registration-lifecycle | ANALYZED | Register/renew via service intents |
| registration-status | ANALYZED | Status, code, reason, expiration, retry |
| account-deletion | ANALYZED | Cleanup by accountId |

## Dependencies

- **Uses**: endpoint-management (initialized endpoint required)
- **Used by**: call-management (all calls require account)

## Key Insights

1. **DTO Pattern**: AccountConfigurationDTO provides type-safe configuration on Android side
2. **JSON Serialization**: Nested regHeaders map serialized to JSONObject
3. **Registration Events**: Separate from account creation (can create without auto-register)
4. **Renew Parameter**: registerAccount supports renew=false for manual unregister
5. **Immutable Models**: Dart Account/AccountRegistration are value objects

## ADR Candidates

- **ADR: Account immutability** - Value objects prevent accidental mutation
- **ADR: Separate registration step** - Create accounts without immediate registration
- **ADR: Custom headers support** - regHeaders for SIP extensions

## Flow Recommendation

- **Type**: SDD (internal service logic, credential management)
- **Confidence**: high
- **Rationale**: Core infrastructure, no stakeholder-facing docs

## Synthesis

> Updated during SYNTHESIZING phase after children complete

### From Children
[Inline analysis - no separate child nodes]

### Combined Understanding

Account management provides:
1. Account creation with full SIP configuration (credentials, transport, headers)
2. Registration lifecycle (register, renew, unregister)
3. Status tracking via AccountRegistration (status, code, expiration)
4. Clean deletion with resource cleanup

The design separates account creation from registration, allowing offline account setup. Registration status is tracked separately via events.

## Bubble Up

> Summary to pass to parent during EXITING

- Account creation with AccountConfigurationDTO (JSON serialization)
- Registration flow: register/renew/unregister
- AccountRegistration model: status, code, expiration, retry
- Account deletion by ID
- Immutable Dart models with fromMap/toMap

## Dependencies

- **Uses**: endpoint-management (requires initialized SIP endpoint)
- **Used by**: call-management (calls require accounts)

## Key Insights

1. [pending analysis]

## ADR Candidates

- Account model immutability pattern
- Registration status as separate class vs embedded

## Flow Recommendation

- **Type**: SDD (internal service logic)
- **Confidence**: high
- **Rationale**: Core infrastructure, credential management

## Synthesis

> Updated during SYNTHESIZING phase after children complete

### From Children
[pending]

### Combined Understanding
[pending]

## Bubble Up

> Summary to pass to parent during EXITING

- [pending]

---

*Phase: ENTERING | Depth: 1 | Parent: root*
