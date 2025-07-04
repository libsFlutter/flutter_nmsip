# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.0.1] - 2024-01-XX

### Added
- Initial release of flutter_sip2 plugin
- SIP endpoint initialization and management
- Account creation, registration, and deletion
- Call management (make, answer, hangup, decline)
- Call control (hold, unhold, mute, unmute, speaker, earpiece)
- DTMF support for sending tones during calls
- Call transfer and redirect functionality
- Event streaming for real-time SIP events
- Background processing support for Android
- Comprehensive API documentation
- Android native implementation with PJSIP library
- Support for audio calls (video support planned)
- STUN server configuration
- Codec settings management
- Network configuration options
- Service configuration management
- Error handling with custom exceptions
- Permission management for Android
- Call state tracking and duration calculation
- Remote number parsing and formatting
- Account registration status monitoring
- Connectivity change detection

### Technical Details
- Kotlin-based Android implementation
- Flutter method channel communication
- Event channel for real-time events
- Broadcast receiver for service communication
- Background service for SIP processing
- JSON-based data serialization
- Comprehensive error handling
- Memory management for SIP objects
- Audio device management
- Wake lock management for call processing

### Dependencies
- Flutter SDK >= 3.0.0
- Android API level 21+
- PJSIP library (to be integrated)
- OpenH264 library (to be integrated)

### Known Limitations
- iOS support not yet implemented
- Video call support not yet implemented
- SIP message support not yet implemented
- Push notification support not yet implemented
- Actual PJSIP library integration pending
- Native library compilation setup pending
