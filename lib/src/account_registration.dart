/// Represents the registration status of a SIP account
class AccountRegistration {
  final bool status;
  final int? code;
  final String? reason;
  final int? expiration;
  final int? retryAfter;

  AccountRegistration({
    required this.status,
    this.code,
    this.reason,
    this.expiration,
    this.retryAfter,
  });

  /// Create an AccountRegistration from a map
  factory AccountRegistration.fromMap(Map<String, dynamic> map) {
    return AccountRegistration(
      status: map['status'] as bool? ?? false,
      code: map['code'] as int?,
      reason: map['reason'] as String?,
      expiration: map['expiration'] as int?,
      retryAfter: map['retryAfter'] as int?,
    );
  }

  /// Convert to map
  Map<String, dynamic> toMap() {
    return {
      'status': status,
      'code': code,
      'reason': reason,
      'expiration': expiration,
      'retryAfter': retryAfter,
    };
  }

  @override
  String toString() {
    return 'AccountRegistration(status: $status, code: $code, reason: $reason)';
  }
} 