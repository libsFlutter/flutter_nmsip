# Specifications: SIP Endpoint Management

> Technical specification for endpoint initialization, configuration, and event streaming.

## Architecture Overview

```
┌─────────────────┐
│   Flutter App   │
└────────┬────────┘
         │ Dart API
         ▼
┌─────────────────┐
│  FlutterSip2    │  ← MethodChannel "flutter_sip2"
│   (Facade)      │  ← EventChannel "flutter_sip2_events"
└────────┬────────┘
         │ Platform Messages
         ▼
┌─────────────────┐
│FlutterSip2Plugin│  ← Kotlin: MethodCallHandler
│   (Android)     │  ← EventChannel StreamHandler
└────────┬────────┘
         │ Intents + callback_id
         ▼
┌─────────────────┐
│  PjSipService   │  ← Background service
│   (Native)      │  ← PJSIP library
└────────┬────────┘
         │ Broadcast Intents
         ▼
┌─────────────────┐
│PjSipBroadcast   │  ← Event broadcasting
│   Receiver      │  ← Callback resolution
└─────────────────┘
```

## Component Specifications

### 1. FlutterSip2 (Dart Facade)

**File**: `lib/flutter_nmsip.dart`

#### Static Properties

```dart
static const MethodChannel _channel = MethodChannel('flutter_sip2');
static const EventChannel _eventChannel = EventChannel('flutter_sip2_events');
static Stream<Map<String, dynamic>>? _eventStream;
```

#### Methods

| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `start` | `Map<String, dynamic>? configuration` | `Future<Map<String, dynamic>>` | Initialize SIP endpoint |
| `changeServiceConfiguration` | `Map<String, dynamic> configuration` | `Future<void>` | Update service settings |
| `changeNetworkConfiguration` | `Map<String, dynamic> configuration` | `Future<void>` | Update network settings |
| `changeCodecSettings` | `Map<String, dynamic> codecSettings` | `Future<void>` | Configure codecs |
| `updateStunServers` | `int accountId, List<String> stunServerList` | `Future<void>` | Update STUN servers |

#### Exception Handling

```dart
try {
  final result = await _channel.invokeMethod('start', configuration);
  return Map<String, dynamic>.from(result);
} on PlatformException catch (e) {
  throw FlutterSip2Exception(e.code, e.message);
}
```

### 2. FlutterSip2Plugin (Kotlin)

**File**: `android/src/main/kotlin/org/tele/flutter_sip2/FlutterSip2Plugin.kt`

#### Platform Channel Registration

```kotlin
override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
  context = flutterPluginBinding.applicationContext
  
  // MethodChannel for commands
  channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_sip2")
  channel.setMethodCallHandler(this)

  // EventChannel for real-time events
  eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "flutter_sip2_events")
  eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
      PjSipBroadcastReceiver.setEventSink(events)
    }
    override fun onCancel(arguments: Any?) {
      PjSipBroadcastReceiver.setEventSink(null)
    }
  })

  // BroadcastReceiver for service responses
  broadcastReceiver = PjSipBroadcastReceiver()
  context.registerReceiver(broadcastReceiver, broadcastReceiver.filter)
}
```

#### Method Call Handler

```kotlin
override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
  when (call.method) {
    "start" -> {
      val configuration = call.arguments as? Map<String, Any>
      val callbackId = broadcastReceiver.register(result)
      val intent = PjActions.createStartIntent(callbackId, configuration, context)
      context.startService(intent)
    }
    "changeServiceConfiguration" -> {
      val configuration = call.arguments as? Map<String, Any>
      val callbackId = broadcastReceiver.register(result)
      val intent = PjActions.createSetServiceConfigurationIntent(callbackId, configuration, context)
      context.startService(intent)
    }
    // ... other methods
  }
}
```

### 3. PjActions (Intent Factory)

**File**: `android/src/main/java/org/tele/flutter_sip2/PjActions.java`

#### Intent Creation Pattern

```java
public static Intent createStartIntent(int callbackId, Map<String, Object> configuration, Context context) {
    Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
    intent.setAction(PjActions.ACTION_START);
    intent.putExtra("callback_id", callbackId);
    formatIntent(intent, configuration);
    return intent;
}

public static Intent createSetServiceConfigurationIntent(int callbackId, Map<String, Object> configuration, Context context) {
    Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
    intent.setAction(PjActions.ACTION_SET_SERVICE_CONFIGURATION);
    intent.putExtra("callback_id", callbackId);
    formatIntent(intent, configuration);
    return intent;
}
```

#### Action Constants

```java
public static final String ACTION_START = "start";
public static final String ACTION_SET_SERVICE_CONFIGURATION = "set_service_configuration";
public static final String ACTION_CHANGE_CODEC_SETTINGS = "change_codec_settings";
```

### 4. PjSipBroadcastReceiver (Event Broadcasting)

**File**: `android/src/main/java/org/tele/flutter_sip2/PjSipBroadcastReceiver.java`

#### Callback Registration

```java
private HashMap<Integer, io.flutter.plugin.common.MethodChannel.Result> callbacks = new HashMap<>();
private static io.flutter.plugin.common.EventChannel.EventSink eventSink;

public int register(io.flutter.plugin.common.MethodChannel.Result callback) {
    int id = ++seq;
    callbacks.put(id, callback);
    return id;
}
```

#### Event Filtering

```java
public IntentFilter getFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(PjActions.EVENT_STARTED);
    filter.addAction(PjActions.EVENT_ACCOUNT_CREATED);
    filter.addAction(PjActions.EVENT_REGISTRATION_CHANGED);
    filter.addAction(PjActions.EVENT_CALL_RECEIVED);
    filter.addAction(PjActions.EVENT_CALL_CHANGED);
    filter.addAction(PjActions.EVENT_CALL_TERMINATED);
    filter.addAction(PjActions.EVENT_CALL_SCREEN_LOCKED);
    filter.addAction(PjActions.EVENT_MESSAGE_RECEIVED);
    filter.addAction(PjActions.EVENT_HANDLED);
    return filter;
}
```

#### Callback Resolution

```java
private void onCallback(Intent intent) {
    io.flutter.plugin.common.MethodChannel.Result callback = null;

    if (intent.hasExtra("callback_id")) {
        int id = intent.getIntExtra("callback_id", -1);
        if (callbacks.containsKey(id)) {
            callback = callbacks.remove(id);
        }
    }

    if (callback == null) return;

    if (intent.hasExtra("exception")) {
        callback.error("SIP_ERROR", intent.getStringExtra("exception"), null);
    } else if (intent.hasExtra("data")) {
        Object params = ArgumentUtils.fromJson(intent.getStringExtra("data"));
        callback.success(params);
    } else {
        callback.success(true);
    }
}
```

#### Event Emission

```java
private void emit(String eventName, @Nullable Object data) {
    if (eventSink != null) {
        Map<String, Object> event = new HashMap<>();
        event.put("event", eventName);
        event.put("data", data);
        eventSink.success(event);
    }
}
```

## Data Models

### Endpoint (Dart)

**File**: `lib/src/endpoint.dart`

```dart
class Endpoint {
  final List<Account> accounts;
  final List<Call> calls;
  final Map<String, dynamic> settings;
  final bool connectivity;

  Endpoint({
    required this.accounts,
    required this.calls,
    required this.settings,
    required this.connectivity,
  });

  factory Endpoint.fromMap(Map<String, dynamic> map) {
    // Parse accounts and calls from nested maps
  }

  Map<String, dynamic> toMap() {
    return {
      'accounts': accounts.map((account) => account.toMap()).toList(),
      'calls': calls.map((call) => call.toMap()).toList(),
      'settings': settings,
      'connectivity': connectivity,
    };
  }
}
```

## Event Types

| Event Name | Data Structure | Trigger |
|------------|----------------|---------|
| `pjSipRegistrationChanged` | `{account_id, status, code, reason}` | Account registration status updated |
| `pjSipCallReceived` | `{call: Call, account: Account}` | Incoming call notification |
| `pjSipCallChanged` | `{call: Call}` | Call state changed |
| `pjSipCallTerminated` | `{call: Call}` | Call ended |
| `pjSipMessageReceived` | `{message: SipMessage}` | SIP message received |
| `pjSipConnectivityChanged` | `{available: bool}` | Network connectivity changed |

## Error Handling

### Exception Types

```dart
class FlutterSip2Exception implements Exception {
  final String code;
  final String? message;

  FlutterSip2Exception(this.code, this.message);

  @override
  String toString() => 'FlutterSip2Exception($code, $message)';
}
```

### Error Codes

| Code | Description |
|------|-------------|
| `SIP_ERROR` | Generic SIP operation failed |
| `PlatformException.code` | Native platform error code |

## Testing Considerations

### Unit Tests (Dart)

- Mock MethodChannel and EventChannel
- Verify exception translation
- Test fromMap/toMap serialization

### Integration Tests

- Platform channel communication
- Event stream subscription
- Callback resolution timing

### Manual Testing

- Background service behavior
- Event broadcast during app lifecycle changes
- Configuration change persistence

---

*Status: DRAFT | Type: SDD | Generated by /legacy*
