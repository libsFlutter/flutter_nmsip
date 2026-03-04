# Understanding: Call Management

> SIP call operations, state machine, media control, and call lifecycle.

## Phase: SYNTHESIZING

## Hypothesis

Call management handles:
- Making outbound calls (audio/video)
- Answering/declining incoming calls
- Call control (hold, unhold, mute, unmute, speaker, earpiece)
- Call transfer and redirect
- DTMF tone transmission
- Call state tracking and duration calculation

## Sources

- lib/flutter_nmsip.dart - All call operation methods
- lib/src/call.dart - Call data class with state machine
- android/src/main/kotlin/FlutterSip2Plugin.kt - Call method handlers
- android/src/main/java/PjActions.java - Call intents
- android/src/main/kotlin/org/tele/flutter_sip2/dto/CallSettingsDTO.kt - Call settings

## Validated Understanding

**Confirmed Architecture:**

1. **Call Operations** (20+ methods):
   - **Lifecycle**: makeCall, answerCall, hangupCall, declineCall
   - **Hold/Unhold**: holdCall, unholdCall
   - **Audio Control**: muteCall, unmuteCall, useSpeaker, useEarpiece
   - **Transfer**: xferCall (blind transfer), redirectCall
   - **DTMF**: dtmfCall (send digits during call)
   - **Progress**: ringingCall, progressCall (early media)

2. **Call Model** (Dart):
   - **Identity**: id, callId, accountId
   - **Participants**: localContact, localUri, remoteContact, remoteUri
   - **State**: state, stateText, held, muted, speaker
   - **Timing**: connectDuration, totalDuration (with real-time calculation)
   - **Media**: audioCount, videoCount, remoteAudioCount, remoteVideoCount
   - **Status**: lastStatusCode, lastReason
   - **Parsed**: remoteNumber, remoteName, localNumber, localName (from URIs)

3. **Call State Machine** (PJSIP states):
   - PJSIP_INV_STATE_NULL: Initial state
   - PJSIP_INV_STATE_CALLING: Outgoing call
   - PJSIP_INV_STATE_INCOMING: Incoming call
   - PJSIP_INV_STATE_EARLY: Early media (ringing)
   - PJSIP_INV_STATE_CONNECTING: Connection in progress
   - PJSIP_INV_STATE_CONFIRMED: Call connected
   - PJSIP_INV_STATE_DISCONNECTED: Call terminated

4. **Duration Tracking**:
   - _constructionTime: Timestamp when Call object created
   - getTotalDuration(): Real-time calculation since construction
   - getConnectDuration(): Connected time (excludes pre-connect)
   - getFormattedTotalDuration(): "MM:SS" format
   - getFormattedConnectDuration(): "MM:SS" format

5. **URI Parsing**:
   - _parseRemoteNumber(): Extract number from "sip:number@domain"
   - _parseRemoteName(): Extract name from "Name" <sip:number@domain>
   - _parseLocalNumber(): Handle sip: and tel: URIs
   - _parseLocalName(): Extract display name

6. **Call Settings** (CallSettingsDTO):
   - flag: Call flags (bitmask)
   - reqKeyframeMethod: Keyframe request method
   - audCnt: Audio stream count (default 1)
   - vidCnt: Video stream count (default 0)

7. **Message Data** (SipMessageDTO):
   - headers: Custom SIP headers for INVITE
   - Used for call customization (e.g., P-Asserted-Identity)

## Children Identified

> Deeper concepts - analyzed inline (no separate nodes needed)

| Child | Status | Notes |
|-------|--------|-------|
| call-lifecycle | ANALYZED | Make, answer, hangup, decline |
| call-control | ANALYZED | Hold, mute, speaker, earpiece |
| call-transfer | ANALYZED | Blind transfer, redirect |
| dtmf | ANALYZED | DTMF tone transmission |
| duration-tracking | ANALYZED | Real-time calculation with _constructionTime |
| uri-parsing | ANALYZED | Number/name extraction from SIP URIs |

## Dependencies

- **Uses**: endpoint-management, account-management
- **Used by**: event-streaming (call state events)

## Key Insights

1. **Real-time Duration**: Call duration calculated dynamically using _constructionTime offset
2. **Immutable Model**: Call is immutable; state changes create new instances
3. **URI Parsing**: Robust parsing handles multiple URI formats (sip:, tel:, with/without display name)
4. **Comprehensive Control**: 20+ call operations cover all PJSIP features
5. **Video Support**: CallSettingsDTO supports audio/video stream counts
6. **Custom Headers**: msgData allows custom SIP headers in INVITE

## ADR Candidates

- **ADR: Immutable call model** - Value objects for state consistency
- **ADR: Real-time duration calculation** - vs. polling native layer
- **ADR: Comprehensive URI parsing** - Handle all SIP URI formats
- **ADR: Video support in CallSettingsDTO** - Future-proof for video calls

## Flow Recommendation

- **Type**: SDD (internal service logic)
- **Confidence**: high
- **Rationale**: Core infrastructure, comprehensive call control API

## Synthesis

> Updated during SYNTHESIZING phase after children complete

### From Children
[Inline analysis - no separate child nodes]

### Combined Understanding

Call management provides comprehensive SIP call control:
1. Full lifecycle: make, answer, hangup, decline
2. In-call operations: hold, mute, speaker, DTMF, transfer
3. Real-time duration tracking with _constructionTime
4. Robust URI parsing for number/name extraction
5. Video support via CallSettingsDTO
6. Custom SIP headers via msgData

The design uses immutable Call objects with dynamic duration calculation, avoiding native polling overhead.

## Bubble Up

> Summary to pass to parent during EXITING

- 20+ call operations covering full PJSIP API
- Call model with real-time duration tracking
- URI parsing for number/name extraction
- CallSettingsDTO for audio/video configuration
- msgData for custom SIP headers
- Call state machine (7 PJSIP states)

---

*Phase: SYNTHESIZING | Depth: 1 | Parent: root*
