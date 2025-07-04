import 'account_registration.dart';

/// Represents a SIP account configuration and registration status
class Account {
  final int id;
  final String uri;
  final String name;
  final String username;
  final String domain;
  final String password;
  final String? proxy;
  final String? transport;
  final String? contactParams;
  final String? contactUriParams;
  final String? regServer;
  final int? regTimeout;
  final String? regContactParams;
  final Map<String, dynamic>? regHeaders;
  final AccountRegistration registration;

  Account({
    required this.id,
    required this.uri,
    required this.name,
    required this.username,
    required this.domain,
    required this.password,
    this.proxy,
    this.transport,
    this.contactParams,
    this.contactUriParams,
    this.regServer,
    this.regTimeout,
    this.regContactParams,
    this.regHeaders,
    required this.registration,
  });

  /// Create an Account from a map
  factory Account.fromMap(Map<String, dynamic> map) {
    return Account(
      id: map['id'] as int,
      uri: map['uri'] as String,
      name: map['name'] as String,
      username: map['username'] as String,
      domain: map['domain'] as String,
      password: map['password'] as String,
      proxy: map['proxy'] as String?,
      transport: map['transport'] as String?,
      contactParams: map['contactParams'] as String?,
      contactUriParams: map['contactUriParams'] as String?,
      regServer: map['regServer'] as String?,
      regTimeout: map['regTimeout'] as int?,
      regContactParams: map['regContactParams'] as String?,
      regHeaders: map['regHeaders'] as Map<String, dynamic>?,
      registration: AccountRegistration.fromMap(map['registration'] as Map<String, dynamic>),
    );
  }

  /// Convert to map
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'uri': uri,
      'name': name,
      'username': username,
      'domain': domain,
      'password': password,
      'proxy': proxy,
      'transport': transport,
      'contactParams': contactParams,
      'contactUriParams': contactUriParams,
      'regServer': regServer,
      'regTimeout': regTimeout,
      'regContactParams': regContactParams,
      'regHeaders': regHeaders,
      'registration': registration.toMap(),
    };
  }

  @override
  String toString() {
    return 'Account(id: $id, name: $name, username: $username, domain: $domain)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is Account && other.id == id;
  }

  @override
  int get hashCode => id.hashCode;
} 