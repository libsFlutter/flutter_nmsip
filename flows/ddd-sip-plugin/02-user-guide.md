# Specifications: Flutter SIP Plugin - Stakeholder Documentation

> Comprehensive documentation for flutter_nmsip stakeholders.

## Quick Start Guide

### 1. Installation

Add to your `pubspec.yaml`:

```yaml
dependencies:
  flutter_nmsip: ^2.1.2
```

### 2. Android Configuration

**Add Permissions** (`android/app/src/main/AndroidManifest.xml`):

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
```

**Request Runtime Permissions** (in your Dart code):

```dart
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  await Permission.microphone.request();
  await Permission.phone.request();
}
```

### 3. Initialize SIP

```dart
import 'package:flutter_nmsip/flutter_nmsip.dart';

// Initialize SIP endpoint
final state = await FlutterSip2.start();
print('SIP initialized: ${state['accounts']?.length ?? 0} accounts');
```

### 4. Create Account

```dart
final account = await FlutterSip2.createAccount({
  'name': 'John Doe',
  'username': '100',
  'domain': 'pbx.yourcompany.com',
  'password': 'your_password',
  'proxy': '192.168.1.100:5060',
  'transport': 'TCP',
});

print('Account created: ${account.name}');
```

### 5. Make a Call

```dart
final call = await FlutterSip2.makeCall(
  account,
  '200@pbx.yourcompany.com',
);

print('Calling ${call.remoteNumber}...');
```

### 6. Handle Incoming Calls

```dart
FlutterSip2.eventStream.listen((event) {
  switch (event['type']) {
    case 'call_received':
      final call = Call.fromMap(event['data']['call']);
      showIncomingCall(call);
      break;
  }
});
```

## Feature Documentation

### Account Management

#### Create Account

```dart
final account = await FlutterSip2.createAccount({
  // Required fields
  'name': 'Display Name',
  'username': 'sip_username',
  'domain': 'sip.domain.com',
  'password': 'secret',
  
  // Optional fields
  'proxy': 'proxy.domain.com:5060',
  'transport': 'TCP',  // TCP, UDP, TLS
  'regServer': 'registrar.domain.com',
  'regTimeout': 3600,  // Registration expiry (seconds)
  'regHeaders': {
    'X-Custom-Header': 'Value',
  },
});
```

**Returns**: `Account` object with:
- `id`: Unique account identifier
- `uri`: Full SIP URI
- `registration`: Registration status

#### Register Account

```dart
// Register (or renew) registration
await FlutterSip2.registerAccount(account, renew: true);

// Unregister
await FlutterSip2.registerAccount(account, renew: false);
```

#### Delete Account

```dart
await FlutterSip2.deleteAccount(account);
```

### Call Operations

#### Make Call

```dart
final call = await FlutterSip2.makeCall(
  account,
  'destination@sip.domain.com',
  callSettings: {
    'audCnt': 1,  // Audio streams
    'vidCnt': 0,  // Video streams (0 = audio only)
  },
  msgData: {
    'headers': {
      'P-Asserted-Identity': 'Caller ID',
    },
  },
);
```

#### Answer Call

```dart
await FlutterSip2.answerCall(call);
```

#### Decline Call

```dart
await FlutterSip2.declineCall(call);
```

#### Hangup Call

```dart
await FlutterSip2.hangupCall(call);
```

### In-Call Controls

#### Hold/Unhold

```dart
await FlutterSip2.holdCall(call);      // Put on hold
await FlutterSip2.unholdCall(call);    // Resume from hold
```

#### Mute/Unmute

```dart
await FlutterSip2.muteCall(call);      // Mute microphone
await FlutterSip2.unmuteCall(call);    // Unmute microphone
```

#### Audio Route

```dart
await FlutterSip2.useSpeaker(call);    // Route to speaker
await FlutterSip2.useEarpiece(call);   // Route to earpiece
```

#### Transfer Call

```dart
// Blind transfer to another number
await FlutterSip2.xferCall(call, 'transfer@destination.com');

// Redirect before answering
await FlutterSip2.redirectCall(call, 'redirect@destination.com');
```

#### Send DTMF

```dart
// Send touch-tone digits (for IVR menus, passwords)
await FlutterSip2.dtmfCall(call, '1234#');
```

### Call Information

```dart
// Call identity
print('Call ID: ${call.id}');
print('Account ID: ${call.accountId}');

// Caller information
print('From: ${call.remoteNumber} (${call.remoteName})');
print('To: ${call.localNumber} (${call.localName})');

// Call state
print('State: ${call.state}');
print('State: ${call.stateText}');

// Call flags
print('Held: ${call.held}');
print('Muted: ${call.muted}');
print('Speaker: ${call.speaker}');

// Duration
print('Duration: ${call.getFormattedConnectDuration()}');
print('Total: ${call.getFormattedTotalDuration()}');

// Media
print('Audio streams: ${call.audioCount}');
print('Video streams: ${call.videoCount}');
```

## Event Handling

### Subscribe to Events

```dart
FlutterSip2.eventStream.listen((event) {
  print('Event: ${event['type']}');
  print('Data: ${event['data']}');
  
  switch (event['type']) {
    case 'registration_changed':
      _handleRegistration(event['data']);
      break;
    case 'call_received':
      _handleIncomingCall(event['data']);
      break;
    case 'call_changed':
      _handleCallStateChange(event['data']);
      break;
    case 'call_terminated':
      _handleCallEnded(event['data']);
      break;
    case 'connectivity_changed':
      _handleConnectivity(event['data']['available']);
      break;
  }
});
```

### Event Types

| Event | Data | Description |
|-------|------|-------------|
| `registration_changed` | `{account_id, status, code, reason, expiration}` | Account registration status changed |
| `call_received` | `{call: Call, account: Account}` | Incoming call notification |
| `call_changed` | `{call: Call}` | Call state changed (answered, held, etc.) |
| `call_terminated` | `{call: Call}` | Call ended |
| `connectivity_changed` | `{available: bool}` | Network connectivity changed |

## Error Handling

### Exception Types

```dart
try {
  await FlutterSip2.createAccount(config);
} on FlutterSip2Exception catch (e) {
  // SIP-specific error
  print('SIP Error: ${e.code} - ${e.message}');
} catch (e) {
  // Other errors
  print('Unexpected error: $e');
}
```

### Common Error Codes

| Code | Meaning | Resolution |
|------|---------|------------|
| `SIP_ERROR` | Generic SIP error | Check logs for details |
| `ECONNREFUSED` | Cannot connect to server | Check network, server address |
| `EAUTHFAILED` | Authentication failed | Check username/password |
| `ENOTFOUND` | Domain not found | Check domain configuration |
| `ETIMEOUT` | Request timeout | Check network, server status |

## Architecture Overview

```
┌─────────────────────────────────────────┐
│           Flutter Application           │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │      flutter_nmsip Plugin       │   │
│  │                                 │   │
│  │  - FlutterSip2 (API facade)     │   │
│  │  - Account, Call (data models)  │   │
│  └─────────────┬───────────────────┘   │
└────────────────┼───────────────────────┘
                 │ MethodChannel / EventChannel
┌────────────────┼───────────────────────┐
│                ▼                       │
│         Android Plugin Layer           │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  FlutterSip2Plugin (Kotlin)     │   │
│  │  PjSipBroadcastReceiver         │   │
│  └─────────────┬───────────────────┘   │
└────────────────┼───────────────────────┘
                 │ Intents
┌────────────────┼───────────────────────┐
│                ▼                       │
│         PjSipService (Background)      │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │      PJSIP Library (Native)     │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

## Best Practices

### 1. Initialize Early

Initialize SIP in `initState()` or app startup:

```dart
@override
void initState() {
  super.initState();
  _initializeSip();
}

Future<void> _initializeSip() async {
  await FlutterSip2.start();
  // ...
}
```

### 2. Handle Lifecycle

Clean up on app termination:

```dart
@override
void dispose() {
  // Cancel event subscriptions
  _eventSubscription?.cancel();
  super.dispose();
}
```

### 3. Show Call UI

Use dialogs or dedicated screens for calls:

```dart
void showIncomingCall(Call call) {
  showDialog(
    context: context,
    barrierDismissible: false,
    builder: (context) => AlertDialog(
      title: Text('Incoming Call'),
      content: Text('From: ${call.remoteNumber}'),
      actions: [
        TextButton(
          onPressed: () async {
            await FlutterSip2.declineCall(call);
            Navigator.pop(context);
          },
          child: Text('Decline'),
        ),
        TextButton(
          onPressed: () async {
            await FlutterSip2.answerCall(call);
            Navigator.pop(context);
          },
          child: Text('Answer'),
        ),
      ],
    ),
  );
}
```

### 4. Update UI on Events

Use StreamBuilder or setState for real-time updates:

```dart
StreamBuilder(
  stream: FlutterSip2.eventStream,
  builder: (context, snapshot) {
    if (snapshot.hasData) {
      final event = snapshot.data as Map<String, dynamic>;
      // Update UI based on event
    }
    return CallScreen();
  },
)
```

### 5. Handle Errors Gracefully

Always wrap SIP calls in try-catch:

```dart
Future<void> makeCall() async {
  try {
    final call = await FlutterSip2.makeCall(account, destination);
    // Success
  } on FlutterSip2Exception catch (e) {
    // Show error to user
    showError('Call failed: ${e.message}');
  }
}
```

## Troubleshooting

### Common Issues

**Issue**: Registration fails

**Solutions**:
1. Check username/password
2. Verify domain/proxy addresses
3. Check network connectivity
4. Review SIP server logs

**Issue**: One-way audio

**Solutions**:
1. Check firewall settings
2. Configure STUN servers
3. Verify codec compatibility
4. Check NAT traversal settings

**Issue**: Calls drop after 30 seconds

**Solutions**:
1. Check registration timeout
2. Verify keep-alive settings
3. Review network stability
4. Check SIP server configuration

## API Reference

### FlutterSip2 Class

| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `start` | `configuration?` | `Future<Map>` | Initialize SIP endpoint |
| `createAccount` | `configuration` | `Future<Account>` | Create SIP account |
| `registerAccount` | `account, renew` | `Future<void>` | Register account |
| `deleteAccount` | `account` | `Future<void>` | Delete account |
| `makeCall` | `account, destination, ...` | `Future<Call>` | Make outbound call |
| `answerCall` | `call` | `Future<void>` | Answer incoming call |
| `hangupCall` | `call` | `Future<void>` | End call |
| `declineCall` | `call` | `Future<void>` | Reject incoming call |
| `holdCall` | `call` | `Future<void>` | Put call on hold |
| `unholdCall` | `call` | `Future<void>` | Resume from hold |
| `muteCall` | `call` | `Future<void>` | Mute microphone |
| `unmuteCall` | `call` | `Future<void>` | Unmute microphone |
| `useSpeaker` | `call` | `Future<void>` | Route to speaker |
| `useEarpiece` | `call` | `Future<void>` | Route to earpiece |
| `dtmfCall` | `call, digits` | `Future<void>` | Send DTMF tones |
| `xferCall` | `call, destination` | `Future<void>` | Transfer call |
| `redirectCall` | `call, destination` | `Future<void>` | Redirect call |

## Support and Resources

### Documentation

- [README.md](../README.md) - Installation and basic usage
- [Example App](../example/lib/main.dart) - Complete working example

### Issue Reporting

- GitHub Issues: Report bugs and feature requests
- Include: Flutter version, device, steps to reproduce

### Version Compatibility

| Plugin Version | Flutter Version | Android API |
|----------------|-----------------|-------------|
| 2.1.2+108 | >= 3.24.0 | >= 21 |

---

*Status: DRAFT | Type: DDD | Generated by /legacy*
