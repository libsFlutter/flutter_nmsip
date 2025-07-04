import 'account.dart';
import 'call.dart';

/// Represents a SIP endpoint that manages accounts and calls
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

  /// Create an Endpoint from a map
  factory Endpoint.fromMap(Map<String, dynamic> map) {
    final accounts = <Account>[];
    final calls = <Call>[];

    if (map['accounts'] != null) {
      for (final accountData in map['accounts'] as List) {
        accounts.add(Account.fromMap(accountData as Map<String, dynamic>));
      }
    }

    if (map['calls'] != null) {
      for (final callData in map['calls'] as List) {
        calls.add(Call.fromMap(callData as Map<String, dynamic>));
      }
    }

    return Endpoint(
      accounts: accounts,
      calls: calls,
      settings: map['settings'] as Map<String, dynamic>? ?? {},
      connectivity: map['connectivity'] as bool? ?? false,
    );
  }

  /// Convert to map
  Map<String, dynamic> toMap() {
    return {
      'accounts': accounts.map((account) => account.toMap()).toList(),
      'calls': calls.map((call) => call.toMap()).toList(),
      'settings': settings,
      'connectivity': connectivity,
    };
  }

  @override
  String toString() {
    return 'Endpoint(accounts: ${accounts.length}, calls: ${calls.length}, connectivity: $connectivity)';
  }
} 