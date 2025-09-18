import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_nmsip/flutter_nmsip.dart';

void main() {
  group('FlutterSip2 Tests', () {
    test('should create Account from map', () {
      final accountData = {
        'id': 1,
        'uri': 'sip:test@example.com',
        'name': 'Test User',
        'username': 'test',
        'domain': 'example.com',
        'password': 'password',
        'proxy': 'proxy.example.com',
        'transport': 'TCP',
        'contactParams': 'test',
        'contactUriParams': null,
        'regServer': 'example.com',
        'regTimeout': 3600,
        'regContactParams': 'test',
        'regHeaders': {'X-Test': 'Value'},
        'registration': {
          'status': true,
          'code': 200,
          'reason': 'OK',
          'expiration': 3600,
          'retryAfter': null,
        }
      };

      final account = Account.fromMap(accountData);
      
      expect(account.id, 1);
      expect(account.name, 'Test User');
      expect(account.username, 'test');
      expect(account.domain, 'example.com');
      expect(account.registration.status, true);
    });

    test('should create Call from map', () {
      final callData = {
        'id': 1,
        'callId': 'call_123',
        'accountId': 1,
        'localContact': 'sip:local@example.com',
        'localUri': 'sip:local@example.com',
        'remoteContact': 'sip:remote@example.com',
        'remoteUri': 'sip:remote@example.com',
        'state': 'PJSIP_INV_STATE_CALLING',
        'stateText': 'Calling',
        'held': false,
        'muted': false,
        'speaker': false,
        'connectDuration': 0,
        'totalDuration': 0,
        'remoteOfferer': false,
        'remoteAudioCount': 1,
        'remoteVideoCount': 0,
        'audioCount': 1,
        'videoCount': 0,
        'lastStatusCode': null,
        'lastReason': null,
        'media': null,
        'provisionalMedia': null,
      };

      final call = Call.fromMap(callData);
      
      expect(call.id, 1);
      expect(call.state, 'PJSIP_INV_STATE_CALLING');
      expect(call.stateText, 'Calling');
      expect(call.held, false);
      expect(call.muted, false);
      expect(call.speaker, false);
    });

    test('should format call duration correctly', () {
      final callData = {
        'id': 1,
        'callId': 'call_123',
        'accountId': 1,
        'state': 'PJSIP_INV_STATE_CONFIRMED',
        'stateText': 'Confirmed',
        'held': false,
        'muted': false,
        'speaker': false,
        'connectDuration': 65,
        'totalDuration': 65,
        'remoteOfferer': false,
        'remoteAudioCount': 1,
        'remoteVideoCount': 0,
        'audioCount': 1,
        'videoCount': 0,
      };

      final call = Call.fromMap(callData);
      
      expect(call.getFormattedConnectDuration(), '01:05');
      expect(call.getFormattedTotalDuration(), '01:05');
    });

    test('should parse remote number from URI', () {
      final callData = {
        'id': 1,
        'callId': 'call_123',
        'accountId': 1,
        'state': 'PJSIP_INV_STATE_CALLING',
        'stateText': 'Calling',
        'held': false,
        'muted': false,
        'speaker': false,
        'connectDuration': 0,
        'totalDuration': 0,
        'remoteOfferer': false,
        'remoteAudioCount': 1,
        'remoteVideoCount': 0,
        'audioCount': 1,
        'videoCount': 0,
        'remoteUri': '"John Doe" <sip:123456@example.com>',
      };

      final call = Call.fromMap(callData);
      
      expect(call.remoteNumber, '123456');
      expect(call.remoteName, 'John Doe');
    });
  });
}
