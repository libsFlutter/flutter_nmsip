# flutter_sip2

A Flutter plugin for SIP (Session Initiation Protocol) communication using PJSIP library.

## Features

- **SIP Account Management**: Create, register, and manage SIP accounts
- **Audio/Video Calls**: Make and receive audio and video calls
- **Call Control**: Answer, hangup, hold, mute, and transfer calls
- **DTMF Support**: Send DTMF tones during calls
- **Background Processing**: Handle calls even when the app is in background
- **Event Streaming**: Real-time events for call state changes
- **Cross-platform**: Android support (iOS support planned)

## Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  flutter_sip2: ^0.0.1
```

## Android Setup

### Permissions

Add the following permissions to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.WRITE_CALL_LOG" />
```

### Runtime Permissions

Request runtime permissions in your app:

```dart
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  await Permission.microphone.request();
  await Permission.phone.request();
}
```

## Usage

### Initialize the SIP Endpoint

First, initialize the SIP endpoint:

```dart
import 'package:flutter_sip2/flutter_sip2.dart';

// Initialize the SIP endpoint
final state = await FlutterSip2.start();
print('SIP initialized with ${state['accounts'].length} accounts and ${state['calls'].length} calls');
```

### Listen to Events

Set up event listeners to handle SIP events:

```dart
FlutterSip2.eventStream.listen((event) {
  switch (event['type']) {
    case 'registration_changed':
      handleRegistrationChanged(event['data']);
      break;
    case 'call_received':
      handleCallReceived(event['data']);
      break;
    case 'call_changed':
      handleCallChanged(event['data']);
      break;
    case 'call_terminated':
      handleCallTerminated(event['data']);
      break;
    case 'connectivity_changed':
      handleConnectivityChanged(event['data']['available']);
      break;
  }
});
```

### Create a SIP Account

```dart
final account = await FlutterSip2.createAccount({
  'name': 'John Doe',
  'username': '100',
  'domain': 'pbx.example.com',
  'password': 'password123',
  'proxy': '192.168.1.100:5060', // optional
  'transport': 'TCP', // optional, default is TCP
  'regServer': 'pbx.example.com', // optional
  'regTimeout': 3600, // optional, default is 3600
  'regHeaders': { // optional
    'X-Custom-Header': 'Value'
  },
  'regContactParams': ';unique-device-token-id=XXXXXXXXX' // optional
});

print('Account created: ${account.name}');
```

### Register the Account

```dart
await FlutterSip2.registerAccount(account, renew: true);
```

### Make a Call

```dart
final call = await FlutterSip2.makeCall(
  account,
  'destination@domain.com',
  callSettings: {
    'flag': 0,
    'reqKeyframeMethod': 0,
    'audCnt': 1,
    'vidCnt': 0
  },
  msgData: {
    'headers': {
      'P-Asserted-Identity': 'Header example',
      'X-UA': 'Flutter SIP2'
    }
  }
);

print('Call initiated: ${call.id}');
```

### Handle Incoming Calls

```dart
void handleCallReceived(Map<String, dynamic> data) {
  final call = Call.fromMap(data['call']);
  final account = Account.fromMap(data['account']);
  
  print('Incoming call from: ${call.remoteNumber}');
  
  // Show incoming call UI
  showIncomingCallDialog(call, account);
}

void showIncomingCallDialog(Call call, Account account) {
  // Show dialog with answer/decline buttons
  showDialog(
    context: context,
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

### Call Control

```dart
// Answer a call
await FlutterSip2.answerCall(call);

// Hangup a call
await FlutterSip2.hangupCall(call);

// Hold a call
await FlutterSip2.holdCall(call);

// Unhold a call
await FlutterSip2.unholdCall(call);

// Mute a call
await FlutterSip2.muteCall(call);

// Unmute a call
await FlutterSip2.unmuteCall(call);

// Use speaker
await FlutterSip2.useSpeaker(call);

// Use earpiece
await FlutterSip2.useEarpiece(call);

// Send DTMF
await FlutterSip2.dtmfCall(call, '123');

// Transfer a call
await FlutterSip2.xferCall(call, 'transfer@domain.com');

// Redirect a call
await FlutterSip2.redirectCall(call, 'redirect@domain.com');
```

### Account Management

```dart
// Delete an account
await FlutterSip2.deleteAccount(account);

// Update STUN servers
await FlutterSip2.updateStunServers(account.id, [
  'stun1.example.com:3478',
  'stun2.example.com:3478'
]);

// Change service configuration
await FlutterSip2.changeServiceConfiguration({
  'userAgent': 'My SIP App',
  'stunServers': ['stun.example.com:3478']
});
```

## API Reference

### FlutterSip2

#### Static Methods

- `start([Map<String, dynamic>? configuration])` - Initialize the SIP endpoint
- `createAccount(Map<String, dynamic> configuration)` - Create a new SIP account
- `registerAccount(Account account, {bool renew = true})` - Register an account
- `deleteAccount(Account account)` - Delete an account
- `makeCall(Account account, String destination, {Map<String, dynamic>? callSettings, Map<String, dynamic>? msgData})` - Make a call
- `answerCall(Call call)` - Answer an incoming call
- `hangupCall(Call call)` - Hangup a call
- `declineCall(Call call)` - Decline an incoming call
- `holdCall(Call call)` - Hold a call
- `unholdCall(Call call)` - Unhold a call
- `muteCall(Call call)` - Mute a call
- `unmuteCall(Call call)` - Unmute a call
- `useSpeaker(Call call)` - Use speaker for a call
- `useEarpiece(Call call)` - Use earpiece for a call
- `dtmfCall(Call call, String digits)` - Send DTMF digits
- `xferCall(Call call, String destination)` - Transfer a call
- `redirectCall(Call call, String destination)` - Redirect a call
- `changeCodecSettings(Map<String, dynamic> codecSettings)` - Change codec settings
- `updateStunServers(int accountId, List<String> stunServerList)` - Update STUN servers
- `changeNetworkConfiguration(Map<String, dynamic> configuration)` - Change network configuration
- `changeServiceConfiguration(Map<String, dynamic> configuration)` - Change service configuration

#### Properties

- `eventStream` - Stream of SIP events

### Account

Represents a SIP account with registration status.

#### Properties

- `id` - Account ID
- `uri` - Account URI
- `name` - Account name
- `username` - Username
- `domain` - Domain
- `password` - Password
- `proxy` - Proxy server (optional)
- `transport` - Transport protocol (optional)
- `registration` - Registration status

### Call

Represents a SIP call with current state and information.

#### Properties

- `id` - Call ID
- `accountId` - Account ID
- `state` - Call state
- `stateText` - Call state text
- `remoteNumber` - Remote number
- `remoteName` - Remote name
- `held` - Whether call is held
- `muted` - Whether call is muted
- `speaker` - Whether speaker is active

#### Methods

- `getId()` - Get call ID
- `getAccountId()` - Get account ID
- `getState()` - Get call state
- `getRemoteNumber()` - Get remote number
- `getRemoteName()` - Get remote name
- `isHeld()` - Check if call is held
- `isMuted()` - Check if call is muted
- `isSpeaker()` - Check if speaker is active
- `isTerminated()` - Check if call is terminated
- `getTotalDuration()` - Get total call duration
- `getConnectDuration()` - Get connected duration
- `getFormattedTotalDuration()` - Get formatted total duration
- `getFormattedConnectDuration()` - Get formatted connected duration

## Events

The plugin emits the following events:

- `registration_changed` - Account registration status changed
- `call_received` - Incoming call received
- `call_changed` - Call state changed
- `call_terminated` - Call terminated
- `call_screen_locked` - Call screen locked (Android only)
- `message_received` - SIP message received
- `connectivity_changed` - Network connectivity changed

## Error Handling

The plugin throws `FlutterSip2Exception` for errors:

```dart
try {
  await FlutterSip2.createAccount(configuration);
} on FlutterSip2Exception catch (e) {
  print('SIP error: ${e.code} - ${e.message}');
}
```

## Background Processing

The plugin supports background processing on Android. Calls can be received and handled even when the app is in the background.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

This plugin is based on the [react-native-sip2](https://github.com/telefon-one/react-native-sip2) library and uses the PJSIP library for SIP functionality.

