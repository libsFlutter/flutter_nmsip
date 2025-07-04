package org.tele.flutter_sip2

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel
import android.content.Intent
import android.content.Context
import org.json.JSONObject

/** FlutterSip2Plugin */
class FlutterSip2Plugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var eventChannel: EventChannel
  private lateinit var context: Context
  private lateinit var broadcastReceiver: PjSipBroadcastReceiver

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_sip2")
    channel.setMethodCallHandler(this)
    
    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "flutter_sip2_events")
    eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
      override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // Store the event sink for later use
        PjSipBroadcastReceiver.setEventSink(events)
      }

      override fun onCancel(arguments: Any?) {
        PjSipBroadcastReceiver.setEventSink(null)
      }
    })
    
    // Initialize broadcast receiver
    broadcastReceiver = PjSipBroadcastReceiver()
    context.registerReceiver(broadcastReceiver, broadcastReceiver.filter)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "start" -> {
        val configuration = call.arguments as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createStartIntent(callbackId, configuration, context)
        context.startService(intent)
      }
      "createAccount" -> {
        val configuration = call.arguments as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createAccountCreateIntent(callbackId, configuration, context)
        context.startService(intent)
      }
      "registerAccount" -> {
        val args = call.arguments as? Map<String, Any>
        val accountId = args?.get("accountId") as? Int ?: 0
        val renew = args?.get("renew") as? Boolean ?: true
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createAccountRegisterIntent(callbackId, accountId, renew, context)
        context.startService(intent)
      }
      "deleteAccount" -> {
        val accountId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createAccountDeleteIntent(callbackId, accountId, context)
        context.startService(intent)
      }
      "makeCall" -> {
        val args = call.arguments as? Map<String, Any>
        val accountId = args?.get("accountId") as? Int ?: 0
        val destination = args?.get("destination") as? String ?: ""
        val callSettings = args?.get("callSettings") as? Map<String, Any>
        val msgData = args?.get("msgData") as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createMakeCallIntent(callbackId, accountId, destination, callSettings, msgData, context)
        context.startService(intent)
      }
      "answerCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createAnswerCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "hangupCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createHangupCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "declineCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createDeclineCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "holdCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createHoldCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "unholdCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createUnholdCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "muteCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createMuteCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "unmuteCall" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createUnMuteCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "useSpeaker" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createUseSpeakerCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "useEarpiece" -> {
        val callId = call.arguments as? Int ?: 0
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createUseEarpieceCallIntent(callbackId, callId, context)
        context.startService(intent)
      }
      "dtmfCall" -> {
        val args = call.arguments as? Map<String, Any>
        val callId = args?.get("callId") as? Int ?: 0
        val digits = args?.get("digits") as? String ?: ""
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createDtmfCallIntent(callbackId, callId, digits, context)
        context.startService(intent)
      }
      "xferCall" -> {
        val args = call.arguments as? Map<String, Any>
        val callId = args?.get("callId") as? Int ?: 0
        val destination = args?.get("destination") as? String ?: ""
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createXFerCallIntent(callbackId, callId, destination, context)
        context.startService(intent)
      }
      "redirectCall" -> {
        val args = call.arguments as? Map<String, Any>
        val callId = args?.get("callId") as? Int ?: 0
        val destination = args?.get("destination") as? String ?: ""
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createRedirectCallIntent(callbackId, callId, destination, context)
        context.startService(intent)
      }
      "changeCodecSettings" -> {
        val codecSettings = call.arguments as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createChangeCodecSettingsIntent(callbackId, codecSettings, context)
        context.startService(intent)
      }
      "updateStunServers" -> {
        val args = call.arguments as? Map<String, Any>
        val accountId = args?.get("accountId") as? Int ?: 0
        val stunServerList = args?.get("stunServerList") as? List<String> ?: emptyList()
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createUpdateStunServersIntent(callbackId, accountId, stunServerList, context)
        context.startService(intent)
      }
      "changeNetworkConfiguration" -> {
        val configuration = call.arguments as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createChangeNetworkConfigurationIntent(callbackId, configuration, context)
        context.startService(intent)
      }
      "changeServiceConfiguration" -> {
        val configuration = call.arguments as? Map<String, Any>
        val callbackId = broadcastReceiver.register(result)
        val intent = PjActions.createSetServiceConfigurationIntent(callbackId, configuration, context)
        context.startService(intent)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    try {
      context.unregisterReceiver(broadcastReceiver)
    } catch (e: Exception) {
      // Receiver might not be registered
    }
  }
}
