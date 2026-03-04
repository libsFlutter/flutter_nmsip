# Understanding: Endpoint Management

> SIP endpoint initialization, configuration, and connectivity management.

## Phase: SYNTHESIZING

## Hypothesis

The endpoint is the core SIP infrastructure that:
- Initializes the native PJSIP stack via platform channels
- Manages global SIP settings (codecs, STUN servers, network config)
- Tracks connectivity state
- Holds collections of accounts and calls

## Sources

- lib/flutter_nmsip.dart - FlutterSip2.start(), change* methods
- lib/src/endpoint.dart - Endpoint data class
- android/src/main/kotlin/FlutterSip2Plugin.kt - Platform channel handler
- android/src/main/java/PjActions.java - Intent creation for service commands
- android/src/main/java/PjSipBroadcastReceiver.java - Event broadcasting

## Validated Understanding

**Confirmed Architecture:**

1. **Platform Channel Setup**:
   - MethodChannel: "flutter_sip2" for commands
   - EventChannel: "flutter_sip2_events" for real-time events
   - Registered in onAttachedToEngine, cleaned up in onDetachedFromEngine

2. **Service-Based Architecture**:
   - All operations route through PjSipService via Intents
   - Each method call creates Intent with callback_id
   - BroadcastReceiver listens for completion events

3. **Callback Pattern**:
   - PjSipBroadcastReceiver.register() stores MethodChannel.Result
   - Callback ID passed via Intent extra
   - Service responds with EVENT_HANDLED or error
   - Callback removed from map after execution

4. **Event Streaming**:
   - Static eventSink in PjSipBroadcastReceiver
   - Events: registration_changed, call_received, call_changed, call_terminated, message_received, connectivity_changed
   - Emitted via emit() method with event name and data

5. **Configuration Methods**:
   - changeServiceConfiguration: User agent, STUN servers
   - changeNetworkConfiguration: Network settings
   - changeCodecSettings: Audio/video codec preferences
   - updateStunServers: Per-account STUN server list

## Children Identified

> Deeper concepts - analyzed inline (no separate nodes needed)

| Child | Status | Notes |
|-------|--------|-------|
| initialization | ANALYZED | Simple platform channel setup |
| configuration | ANALYZED | 3 config methods + updateStunServers |
| connectivity | ANALYZED | Tracked via connectivity_changed events |

## Dependencies

- **Uses**: native-bridge (platform channels, event broadcasting)
- **Used by**: account-management, call-management (all operations require initialized endpoint)

## Key Insights

1. **Service Intent Pattern**: All SIP operations use Android Intents to communicate with PjSipService, enabling background execution
2. **Callback Registration**: Sequential callback IDs (seq++) prevent race conditions
3. **Event Broadcast Design**: Static eventSink allows service to broadcast without Flutter engine reference
4. **JSON Serialization**: Data passed via JSON strings in Intent extras (ArgumentUtils.fromJson/toJson)

## ADR Candidates

- **ADR: Service-based architecture** - Why Intents vs direct method calls? (Background processing, lifecycle independence)
- **ADR: EventChannel vs MethodChannel polling** - Real-time events for call state changes
- **ADR: JSON serialization for Intent extras** - Type-safe data transfer across process boundaries

## Flow Recommendation

- **Type**: SDD (internal service logic)
- **Confidence**: high
- **Rationale**: Core infrastructure, no stakeholder-facing documentation needed

## Synthesis

> Updated during SYNTHESIZING phase after children complete

### From Children
[Inline analysis - no separate child nodes]

### Combined Understanding

Endpoint management is the foundation layer that:
1. Initializes platform channels (MethodChannel + EventChannel)
2. Routes all SIP commands through Android service via Intents
3. Manages callback registration for async operations
4. Broadcasts real-time events to Flutter layer
5. Provides configuration methods for service/network/codec settings

The design enables background processing by decoupling Flutter from PjSipService through Intent-based communication.

## Bubble Up

> Summary to pass to parent during EXITING

- Platform channels: MethodChannel "flutter_sip2" + EventChannel "flutter_sip2_events"
- Service-based architecture with Intent routing
- Callback pattern with sequential ID registration
- Event broadcasting for real-time updates
- Configuration methods for service/network/codec

## Dependencies

- **Uses**: native-bridge (platform channels)
- **Used by**: account-management, call-management

## Key Insights

1. [pending analysis]

## ADR Candidates

- Platform channel naming convention ('flutter_sip2')
- EventChannel for broadcast stream vs MethodChannel for commands

## Flow Recommendation

- **Type**: SDD (internal service logic, no stakeholder-facing docs)
- **Confidence**: high
- **Rationale**: Core infrastructure, not user-visible feature

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
