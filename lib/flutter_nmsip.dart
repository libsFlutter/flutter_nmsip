
import 'dart:async';

import 'package:flutter/services.dart';

import 'src/account.dart';
import 'src/call.dart';

export 'src/account.dart';
export 'src/call.dart';

class FlutterSip2 {
  static const MethodChannel _channel = MethodChannel('flutter_sip2');

  static const EventChannel _eventChannel = EventChannel('flutter_sip2_events');

  static Stream<Map<String, dynamic>>? _eventStream;

  /// Get the event stream for SIP events
  static Stream<Map<String, dynamic>> get eventStream {
    _eventStream ??= _eventChannel
        .receiveBroadcastStream()
        .map((event) => Map<String, dynamic>.from(event));
    return _eventStream!;
  }

  /// Initialize the SIP endpoint
  static Future<Map<String, dynamic>> start([Map<String, dynamic>? configuration]) async {
    try {
      final result = await _channel.invokeMethod('start', configuration);
      return Map<String, dynamic>.from(result);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Create a new SIP account
  static Future<Account> createAccount(Map<String, dynamic> configuration) async {
    try {
      final result = await _channel.invokeMethod('createAccount', configuration);
      return Account.fromMap(result);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Register an account
  static Future<void> registerAccount(Account account, {bool renew = true}) async {
    try {
      await _channel.invokeMethod('registerAccount', {
        'accountId': account.id,
        'renew': renew,
      });
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Delete an account
  static Future<void> deleteAccount(Account account) async {
    try {
      await _channel.invokeMethod('deleteAccount', account.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Make a call
  static Future<Call> makeCall(
    Account account,
    String destination, {
    Map<String, dynamic>? callSettings,
    Map<String, dynamic>? msgData,
  }) async {
    try {
      final result = await _channel.invokeMethod('makeCall', {
        'accountId': account.id,
        'destination': destination,
        'callSettings': callSettings,
        'msgData': msgData,
      });
      return Call.fromMap(result);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Answer a call
  static Future<void> answerCall(Call call) async {
    try {
      await _channel.invokeMethod('answerCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Hangup a call
  static Future<void> hangupCall(Call call) async {
    try {
      await _channel.invokeMethod('hangupCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Decline a call
  static Future<void> declineCall(Call call) async {
    try {
      await _channel.invokeMethod('declineCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Hold a call
  static Future<void> holdCall(Call call) async {
    try {
      await _channel.invokeMethod('holdCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Unhold a call
  static Future<void> unholdCall(Call call) async {
    try {
      await _channel.invokeMethod('unholdCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Mute a call
  static Future<void> muteCall(Call call) async {
    try {
      await _channel.invokeMethod('muteCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Unmute a call
  static Future<void> unmuteCall(Call call) async {
    try {
      await _channel.invokeMethod('unmuteCall', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Use speaker for a call
  static Future<void> useSpeaker(Call call) async {
    try {
      await _channel.invokeMethod('useSpeaker', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Use earpiece for a call
  static Future<void> useEarpiece(Call call) async {
    try {
      await _channel.invokeMethod('useEarpiece', call.id);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Send DTMF digits
  static Future<void> dtmfCall(Call call, String digits) async {
    try {
      await _channel.invokeMethod('dtmfCall', {
        'callId': call.id,
        'digits': digits,
      });
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Transfer a call
  static Future<void> xferCall(Call call, String destination) async {
    try {
      await _channel.invokeMethod('xferCall', {
        'callId': call.id,
        'destination': destination,
      });
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Redirect a call
  static Future<void> redirectCall(Call call, String destination) async {
    try {
      await _channel.invokeMethod('redirectCall', {
        'callId': call.id,
        'destination': destination,
      });
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Change codec settings
  static Future<void> changeCodecSettings(Map<String, dynamic> codecSettings) async {
    try {
      await _channel.invokeMethod('changeCodecSettings', codecSettings);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Update STUN servers
  static Future<void> updateStunServers(int accountId, List<String> stunServerList) async {
    try {
      await _channel.invokeMethod('updateStunServers', {
        'accountId': accountId,
        'stunServerList': stunServerList,
      });
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Change network configuration
  static Future<void> changeNetworkConfiguration(Map<String, dynamic> configuration) async {
    try {
      await _channel.invokeMethod('changeNetworkConfiguration', configuration);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }

  /// Change service configuration
  static Future<void> changeServiceConfiguration(Map<String, dynamic> configuration) async {
    try {
      await _channel.invokeMethod('changeServiceConfiguration', configuration);
    } on PlatformException catch (e) {
      throw FlutterSip2Exception(e.code, e.message);
    }
  }
}

/// Exception thrown by FlutterSip2
class FlutterSip2Exception implements Exception {
  final String code;
  final String? message;

  FlutterSip2Exception(this.code, this.message);

  @override
  String toString() => 'FlutterSip2Exception($code, $message)';
}
