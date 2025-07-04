/// Represents a SIP call with its current state and information
class Call {
  final int id;
  final int callId;
  final int accountId;
  final String? localContact;
  final String? localUri;
  final String? remoteContact;
  final String? remoteUri;
  final String state;
  final String stateText;
  final bool held;
  final bool muted;
  final bool speaker;
  final int connectDuration;
  final int totalDuration;
  final bool remoteOfferer;
  final int remoteAudioCount;
  final int remoteVideoCount;
  final int audioCount;
  final int videoCount;
  final int? lastStatusCode;
  final String? lastReason;
  final Map<String, dynamic>? media;
  final Map<String, dynamic>? provisionalMedia;

  // Parsed information
  final String? remoteNumber;
  final String? remoteName;
  final String? localNumber;
  final String? localName;

  // Internal tracking
  final int _constructionTime;

  Call({
    required this.id,
    required this.callId,
    required this.accountId,
    this.localContact,
    this.localUri,
    this.remoteContact,
    this.remoteUri,
    required this.state,
    required this.stateText,
    required this.held,
    required this.muted,
    required this.speaker,
    required this.connectDuration,
    required this.totalDuration,
    required this.remoteOfferer,
    required this.remoteAudioCount,
    required this.remoteVideoCount,
    required this.audioCount,
    required this.videoCount,
    this.lastStatusCode,
    this.lastReason,
    this.media,
    this.provisionalMedia,
  }) : _constructionTime = DateTime.now().millisecondsSinceEpoch ~/ 1000,
       remoteNumber = _parseRemoteNumber(remoteUri),
       remoteName = _parseRemoteName(remoteUri),
       localNumber = _parseLocalNumber(localUri),
       localName = _parseLocalName(localUri);

  /// Create a Call from a map
  factory Call.fromMap(Map<String, dynamic> map) {
    return Call(
      id: map['id'] as int,
      callId: map['callId'] as int,
      accountId: map['accountId'] as int,
      localContact: map['localContact'] as String?,
      localUri: map['localUri'] as String?,
      remoteContact: map['remoteContact'] as String?,
      remoteUri: map['remoteUri'] as String?,
      state: map['state'] as String,
      stateText: map['stateText'] as String,
      held: map['held'] as bool? ?? false,
      muted: map['muted'] as bool? ?? false,
      speaker: map['speaker'] as bool? ?? false,
      connectDuration: map['connectDuration'] as int? ?? 0,
      totalDuration: map['totalDuration'] as int? ?? 0,
      remoteOfferer: map['remoteOfferer'] as bool? ?? false,
      remoteAudioCount: map['remoteAudioCount'] as int? ?? 0,
      remoteVideoCount: map['remoteVideoCount'] as int? ?? 0,
      audioCount: map['audioCount'] as int? ?? 0,
      videoCount: map['videoCount'] as int? ?? 0,
      lastStatusCode: map['lastStatusCode'] as int?,
      lastReason: map['lastReason'] as String?,
      media: map['media'] as Map<String, dynamic>?,
      provisionalMedia: map['provisionalMedia'] as Map<String, dynamic>?,
    );
  }

  /// Convert to map
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'callId': callId,
      'accountId': accountId,
      'localContact': localContact,
      'localUri': localUri,
      'remoteContact': remoteContact,
      'remoteUri': remoteUri,
      'state': state,
      'stateText': stateText,
      'held': held,
      'muted': muted,
      'speaker': speaker,
      'connectDuration': connectDuration,
      'totalDuration': totalDuration,
      'remoteOfferer': remoteOfferer,
      'remoteAudioCount': remoteAudioCount,
      'remoteVideoCount': remoteVideoCount,
      'audioCount': audioCount,
      'videoCount': videoCount,
      'lastStatusCode': lastStatusCode,
      'lastReason': lastReason,
      'media': media,
      'provisionalMedia': provisionalMedia,
    };
  }

  /// Get the call ID
  int getId() => id;

  /// Get the account ID where this call belongs
  int getAccountId() => accountId;

  /// Get the dialog Call-ID string
  int getCallId() => callId;

  /// Get up-to-date call duration in seconds
  int getTotalDuration() {
    final time = DateTime.now().millisecondsSinceEpoch ~/ 1000;
    final offset = time - _constructionTime;
    return totalDuration + offset;
  }

  /// Get up-to-date call connected duration
  int getConnectDuration() {
    if (connectDuration < 0 || state == "PJSIP_INV_STATE_DISCONNECTED") {
      return connectDuration;
    }
    final time = DateTime.now().millisecondsSinceEpoch ~/ 1000;
    final offset = time - _constructionTime;
    return connectDuration + offset;
  }

  /// Get call duration in "MM:SS" format
  String getFormattedTotalDuration() => _formatTime(getTotalDuration());

  /// Get connected duration in "MM:SS" format
  String getFormattedConnectDuration() => _formatTime(getConnectDuration());

  /// Get local contact
  String? getLocalContact() => localContact;

  /// Get local URI
  String? getLocalUri() => localUri;

  /// Get remote contact
  String? getRemoteContact() => remoteContact;

  /// Get remote URI
  String? getRemoteUri() => remoteUri;

  /// Get remote name
  String? getRemoteName() => remoteName;

  /// Get remote number
  String? getRemoteNumber() => remoteNumber;

  /// Get formatted remote number
  String? getRemoteFormattedNumber() {
    if (remoteNumber == null) return null;
    
    // Remove any non-digit characters except + and -
    final cleaned = remoteNumber!.replaceAll(RegExp(r'[^\d+\-]'), '');
    
    // If it starts with +, keep it as is
    if (cleaned.startsWith('+')) {
      return cleaned;
    }
    
    // If it's a local number (10 digits), format as (XXX) XXX-XXXX
    if (cleaned.length == 10) {
      return '(${cleaned.substring(0, 3)}) ${cleaned.substring(3, 6)}-${cleaned.substring(6)}';
    }
    
    // If it's a 7-digit number, format as XXX-XXXX
    if (cleaned.length == 7) {
      return '${cleaned.substring(0, 3)}-${cleaned.substring(3)}';
    }
    
    return cleaned;
  }

  /// Get call state
  String getState() => state;

  /// Get call state text
  String getStateText() => stateText;

  /// Check if call is held
  bool isHeld() => held;

  /// Check if call is muted
  bool isMuted() => muted;

  /// Check if speaker is active
  bool isSpeaker() => speaker;

  /// Check if call is terminated
  bool isTerminated() => state == "PJSIP_INV_STATE_DISCONNECTED";

  /// Check if remote is offerer
  bool getRemoteOfferer() => remoteOfferer;

  /// Get remote audio count
  int getRemoteAudioCount() => remoteAudioCount;

  /// Get remote video count
  int getRemoteVideoCount() => remoteVideoCount;

  /// Get audio count
  int getAudioCount() => audioCount;

  /// Get video count
  int getVideoCount() => videoCount;

  /// Get last status code
  int? getLastStatusCode() => lastStatusCode;

  /// Get last reason
  String? getLastReason() => lastReason;

  /// Get media information
  Map<String, dynamic>? getMedia() => media;

  /// Get provisional media information
  Map<String, dynamic>? getProvisionalMedia() => provisionalMedia;

  /// Format time in MM:SS format
  String _formatTime(int seconds) {
    if (seconds < 0) return "00:00";
    
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    
    return "${minutes.toString().padLeft(2, '0')}:${remainingSeconds.toString().padLeft(2, '0')}";
  }

  /// Parse remote number from URI
  static String? _parseRemoteNumber(String? remoteUri) {
    if (remoteUri == null) return null;
    
    // Try to match "Name" <sip:number@domain>
    final match1 = RegExp(r'"([^"]+)" <sip:([^@]+)@').firstMatch(remoteUri);
    if (match1 != null) {
      return match1.group(2);
    }
    
    // Try to match sip:number@domain
    final match2 = RegExp(r'sip:([^@]+)@').firstMatch(remoteUri);
    if (match2 != null) {
      return match2.group(1);
    }
    
    return null;
  }

  /// Parse remote name from URI
  static String? _parseRemoteName(String? remoteUri) {
    if (remoteUri == null) return null;
    
    final match = RegExp(r'"([^"]+)" <sip:([^@]+)@').firstMatch(remoteUri);
    return match?.group(1);
  }

  /// Parse local number from URI
  static String? _parseLocalNumber(String? localUri) {
    if (localUri == null) return null;
    
    // Try to match "Name" <sip:number@domain>
    final match1 = RegExp(r'"([^"]+)" <sip:([^@]+)@').firstMatch(localUri);
    if (match1 != null) {
      return match1.group(2);
    }
    
    // Try to match sip:number@domain
    final match2 = RegExp(r'sip:([^@]+)@').firstMatch(localUri);
    if (match2 != null) {
      return match2.group(1);
    }
    
    // Try to match tel:number
    final match3 = RegExp(r'tel:([^@]+)').firstMatch(localUri);
    if (match3 != null) {
      return Uri.decodeComponent(match3.group(1)!);
    }
    
    return null;
  }

  /// Parse local name from URI
  static String? _parseLocalName(String? localUri) {
    if (localUri == null) return null;
    
    final match = RegExp(r'"([^"]+)" <sip:([^@]+)@').firstMatch(localUri);
    return match?.group(1);
  }

  @override
  String toString() {
    return 'Call(id: $id, state: $state, remoteNumber: $remoteNumber, remoteName: $remoteName)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is Call && other.id == id;
  }

  @override
  int get hashCode => id.hashCode;
} 