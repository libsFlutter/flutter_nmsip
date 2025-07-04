import 'package:flutter/material.dart';
import 'package:flutter_sip2/flutter_sip2.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter SIP2 Example',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter SIP2 Example'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  Account? _account;
  Call? _currentCall;
  String _status = 'Initializing...';
  bool _isInitialized = false;

  @override
  void initState() {
    super.initState();
    _initializeSip();
    _setupEventListeners();
  }

  Future<void> _initializeSip() async {
    try {
      setState(() {
        _status = 'Initializing SIP...';
      });

      final state = await FlutterSip2.start();
      
      setState(() {
        _isInitialized = true;
        _status = 'SIP initialized successfully';
      });

      print('SIP initialized with ${state['accounts']?.length ?? 0} accounts and ${state['calls']?.length ?? 0} calls');
    } catch (e) {
      setState(() {
        _status = 'Failed to initialize SIP: $e';
      });
      print('Failed to initialize SIP: $e');
    }
  }

  void _setupEventListeners() {
    FlutterSip2.eventStream.listen((event) {
      print('SIP Event: $event');
      
      switch (event['type']) {
        case 'registration_changed':
          _handleRegistrationChanged(event['data']);
          break;
        case 'call_received':
          _handleCallReceived(event['data']);
          break;
        case 'call_changed':
          _handleCallChanged(event['data']);
          break;
        case 'call_terminated':
          _handleCallTerminated(event['data']);
          break;
        case 'connectivity_changed':
          _handleConnectivityChanged(event['data']['available']);
          break;
      }
    });
  }

  void _handleRegistrationChanged(Map<String, dynamic> data) {
    setState(() {
      _status = 'Registration status changed';
    });
  }

  void _handleCallReceived(Map<String, dynamic> data) {
    final call = Call.fromMap(data['call']);
    final account = Account.fromMap(data['account']);
    
    setState(() {
      _currentCall = call;
      _status = 'Incoming call from ${call.remoteNumber}';
    });

    _showIncomingCallDialog(call, account);
  }

  void _handleCallChanged(Map<String, dynamic> data) {
    final call = Call.fromMap(data);
    setState(() {
      _currentCall = call;
      _status = 'Call state: ${call.stateText}';
    });
  }

  void _handleCallTerminated(Map<String, dynamic> data) {
    final call = Call.fromMap(data);
    setState(() {
      _currentCall = null;
      _status = 'Call terminated';
    });
  }

  void _handleConnectivityChanged(bool available) {
    setState(() {
      _status = available ? 'Connected' : 'Disconnected';
    });
  }

  void _showIncomingCallDialog(Call call, Account account) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => AlertDialog(
        title: const Text('Incoming Call'),
        content: Text('From: ${call.remoteNumber ?? 'Unknown'}'),
        actions: [
          TextButton(
            onPressed: () async {
              try {
                await FlutterSip2.declineCall(call);
                Navigator.pop(context);
              } catch (e) {
                print('Failed to decline call: $e');
              }
            },
            child: const Text('Decline'),
          ),
          TextButton(
            onPressed: () async {
              try {
                await FlutterSip2.answerCall(call);
                Navigator.pop(context);
              } catch (e) {
                print('Failed to answer call: $e');
              }
            },
            child: const Text('Answer'),
          ),
        ],
      ),
    );
  }

  Future<void> _createAccount() async {
    try {
      setState(() {
        _status = 'Creating account...';
      });

      final account = await FlutterSip2.createAccount({
        'name': 'Test User',
        'username': 'testuser',
        'domain': 'pbx.example.com',
        'password': 'password123',
        'proxy': '192.168.1.100:5060',
        'transport': 'TCP',
        'regServer': 'pbx.example.com',
        'regTimeout': 3600,
      });

      setState(() {
        _account = account;
        _status = 'Account created: ${account.name}';
      });

      // Register the account
      await FlutterSip2.registerAccount(account);
      setState(() {
        _status = 'Account registered';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to create account: $e';
      });
      print('Failed to create account: $e');
    }
  }

  Future<void> _makeCall() async {
    if (_account == null) {
      setState(() {
        _status = 'Please create an account first';
      });
      return;
    }

    try {
      setState(() {
        _status = 'Making call...';
      });

      final call = await FlutterSip2.makeCall(
        _account!,
        'test@pbx.example.com',
        callSettings: {
          'flag': 0,
          'reqKeyframeMethod': 0,
          'audCnt': 1,
          'vidCnt': 0
        },
      );

      setState(() {
        _currentCall = call;
        _status = 'Call initiated: ${call.id}';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to make call: $e';
      });
      print('Failed to make call: $e');
    }
  }

  Future<void> _hangupCall() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.hangupCall(_currentCall!);
      setState(() {
        _currentCall = null;
        _status = 'Call hung up';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to hangup call: $e';
      });
      print('Failed to hangup call: $e');
    }
  }

  Future<void> _muteCall() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.muteCall(_currentCall!);
      setState(() {
        _status = 'Call muted';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to mute call: $e';
      });
      print('Failed to mute call: $e');
    }
  }

  Future<void> _unmuteCall() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.unmuteCall(_currentCall!);
      setState(() {
        _status = 'Call unmuted';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to unmute call: $e';
      });
      print('Failed to unmute call: $e');
    }
  }

  Future<void> _useSpeaker() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.useSpeaker(_currentCall!);
      setState(() {
        _status = 'Speaker enabled';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to enable speaker: $e';
      });
      print('Failed to enable speaker: $e');
    }
  }

  Future<void> _useEarpiece() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.useEarpiece(_currentCall!);
      setState(() {
        _status = 'Earpiece enabled';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to enable earpiece: $e';
      });
      print('Failed to enable earpiece: $e');
    }
  }

  Future<void> _sendDtmf() async {
    if (_currentCall == null) {
      setState(() {
        _status = 'No active call';
      });
      return;
    }

    try {
      await FlutterSip2.dtmfCall(_currentCall!, '123');
      setState(() {
        _status = 'DTMF sent: 123';
      });
    } catch (e) {
      setState(() {
        _status = 'Failed to send DTMF: $e';
      });
      print('Failed to send DTMF: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Status: $_status',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Initialized: $_isInitialized',
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                    if (_account != null) ...[
                      const SizedBox(height: 8),
                      Text(
                        'Account: ${_account!.name} (${_account!.username}@${_account!.domain})',
                        style: Theme.of(context).textTheme.bodyMedium,
                      ),
                    ],
                    if (_currentCall != null) ...[
                      const SizedBox(height: 8),
                      Text(
                        'Call: ${_currentCall!.remoteNumber} (${_currentCall!.stateText})',
                        style: Theme.of(context).textTheme.bodyMedium,
                      ),
                    ],
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            if (!_isInitialized)
              const Card(
                child: Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Text('Initializing SIP...'),
                ),
              )
            else ...[
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      const Text(
                        'Account Management',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      ElevatedButton(
                        onPressed: _account == null ? _createAccount : null,
                        child: const Text('Create Account'),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      const Text(
                        'Call Management',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      ElevatedButton(
                        onPressed: _account != null && _currentCall == null ? _makeCall : null,
                        child: const Text('Make Call'),
                      ),
                      const SizedBox(height: 8),
                      ElevatedButton(
                        onPressed: _currentCall != null ? _hangupCall : null,
                        child: const Text('Hangup Call'),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      const Text(
                        'Call Control',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton(
                              onPressed: _currentCall != null ? _muteCall : null,
                              child: const Text('Mute'),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: ElevatedButton(
                              onPressed: _currentCall != null ? _unmuteCall : null,
                              child: const Text('Unmute'),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton(
                              onPressed: _currentCall != null ? _useSpeaker : null,
                              child: const Text('Speaker'),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: ElevatedButton(
                              onPressed: _currentCall != null ? _useEarpiece : null,
                              child: const Text('Earpiece'),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      ElevatedButton(
                        onPressed: _currentCall != null ? _sendDtmf : null,
                        child: const Text('Send DTMF (123)'),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
