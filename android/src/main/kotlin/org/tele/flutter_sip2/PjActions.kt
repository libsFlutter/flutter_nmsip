package org.tele.flutter_sip2

import android.content.Context
import android.content.Intent
import org.json.JSONObject

object PjActions {
    const val ACTION_START = "org.tele.flutter_sip2.START"
    const val ACTION_SET_SERVICE_CONFIGURATION = "org.tele.flutter_sip2.SET_SERVICE_CONFIGURATION"
    const val ACTION_CREATE_ACCOUNT = "org.tele.flutter_sip2.CREATE_ACCOUNT"
    const val ACTION_REGISTER_ACCOUNT = "org.tele.flutter_sip2.REGISTER_ACCOUNT"
    const val ACTION_DELETE_ACCOUNT = "org.tele.flutter_sip2.DELETE_ACCOUNT"
    const val ACTION_MAKE_CALL = "org.tele.flutter_sip2.MAKE_CALL"
    const val ACTION_HANGUP_CALL = "org.tele.flutter_sip2.HANGUP_CALL"
    const val ACTION_DECLINE_CALL = "org.tele.flutter_sip2.DECLINE_CALL"
    const val ACTION_ANSWER_CALL = "org.tele.flutter_sip2.ANSWER_CALL"
    const val ACTION_RINGING_CALL = "org.tele.flutter_sip2.RINGING_CALL"
    const val ACTION_PROGRESS_CALL = "org.tele.flutter_sip2.PROGRESS_CALL"
    const val ACTION_HOLD_CALL = "org.tele.flutter_sip2.HOLD_CALL"
    const val ACTION_UNHOLD_CALL = "org.tele.flutter_sip2.UNHOLD_CALL"
    const val ACTION_MUTE_CALL = "org.tele.flutter_sip2.MUTE_CALL"
    const val ACTION_UNMUTE_CALL = "org.tele.flutter_sip2.UNMUTE_CALL"
    const val ACTION_USE_SPEAKER_CALL = "org.tele.flutter_sip2.USE_SPEAKER_CALL"
    const val ACTION_USE_EARPIECE_CALL = "org.tele.flutter_sip2.USE_EARPIECE_CALL"
    const val ACTION_XFER_CALL = "org.tele.flutter_sip2.XFER_CALL"
    const val ACTION_XFER_REPLACES_CALL = "org.tele.flutter_sip2.XFER_REPLACES_CALL"
    const val ACTION_REDIRECT_CALL = "org.tele.flutter_sip2.REDIRECT_CALL"
    const val ACTION_DTMF_CALL = "org.tele.flutter_sip2.DTMF_CALL"
    const val ACTION_CHANGE_CODEC_SETTINGS = "org.tele.flutter_sip2.CHANGE_CODEC_SETTINGS"
    const val ACTION_UPDATE_STUN_SERVERS = "org.tele.flutter_sip2.UPDATE_STUN_SERVERS"
    const val ACTION_CHANGE_NETWORK_CONFIGURATION = "org.tele.flutter_sip2.CHANGE_NETWORK_CONFIGURATION"

    const val EXTRA_CALLBACK_ID = "callback_id"
    const val EXTRA_CONFIGURATION = "configuration"
    const val EXTRA_ACCOUNT_ID = "account_id"
    const val EXTRA_RENEW = "renew"
    const val EXTRA_DESTINATION = "destination"
    const val EXTRA_CALL_SETTINGS = "call_settings"
    const val EXTRA_MSG_DATA = "msg_data"
    const val EXTRA_CALL_ID = "call_id"
    const val EXTRA_DIGITS = "digits"
    const val EXTRA_STUN_SERVER_LIST = "stun_server_list"

    fun createStartIntent(callbackId: Int, configuration: Map<String, Any>?, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_START
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        if (configuration != null) {
            intent.putExtra(EXTRA_CONFIGURATION, JSONObject(configuration).toString())
        }
        return intent
    }

    fun createSetServiceConfigurationIntent(callbackId: Int, configuration: Map<String, Any>?, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_SET_SERVICE_CONFIGURATION
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        if (configuration != null) {
            intent.putExtra(EXTRA_CONFIGURATION, JSONObject(configuration).toString())
        }
        return intent
    }

    fun createAccountCreateIntent(callbackId: Int, configuration: Map<String, Any>?, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_CREATE_ACCOUNT
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        if (configuration != null) {
            intent.putExtra(EXTRA_CONFIGURATION, JSONObject(configuration).toString())
        }
        return intent
    }

    fun createAccountRegisterIntent(callbackId: Int, accountId: Int, renew: Boolean, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_REGISTER_ACCOUNT
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId)
        intent.putExtra(EXTRA_RENEW, renew)
        return intent
    }

    fun createAccountDeleteIntent(callbackId: Int, accountId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_DELETE_ACCOUNT
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId)
        return intent
    }

    fun createMakeCallIntent(
        callbackId: Int,
        accountId: Int,
        destination: String,
        callSettings: Map<String, Any>?,
        msgData: Map<String, Any>?,
        context: Context
    ): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_MAKE_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId)
        intent.putExtra(EXTRA_DESTINATION, destination)
        if (callSettings != null) {
            intent.putExtra(EXTRA_CALL_SETTINGS, JSONObject(callSettings).toString())
        }
        if (msgData != null) {
            intent.putExtra(EXTRA_MSG_DATA, JSONObject(msgData).toString())
        }
        return intent
    }

    fun createHangupCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_HANGUP_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createDeclineCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_DECLINE_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createAnswerCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_ANSWER_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createRingingCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_RINGING_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createProgressCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_PROGRESS_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createHoldCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_HOLD_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createUnholdCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_UNHOLD_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createMuteCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_MUTE_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createUnMuteCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_UNMUTE_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createUseSpeakerCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_USE_SPEAKER_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createUseEarpieceCallIntent(callbackId: Int, callId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_USE_EARPIECE_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        return intent
    }

    fun createXFerCallIntent(callbackId: Int, callId: Int, destination: String, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_XFER_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        intent.putExtra(EXTRA_DESTINATION, destination)
        return intent
    }

    fun createXFerReplacesCallIntent(callbackId: Int, callId: Int, destCallId: Int, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_XFER_REPLACES_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        intent.putExtra("dest_call_id", destCallId)
        return intent
    }

    fun createRedirectCallIntent(callbackId: Int, callId: Int, destination: String, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_REDIRECT_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        intent.putExtra(EXTRA_DESTINATION, destination)
        return intent
    }

    fun createDtmfCallIntent(callbackId: Int, callId: Int, digits: String, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_DTMF_CALL
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_CALL_ID, callId)
        intent.putExtra(EXTRA_DIGITS, digits)
        return intent
    }

    fun createChangeCodecSettingsIntent(callbackId: Int, codecSettings: Map<String, Any>?, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_CHANGE_CODEC_SETTINGS
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        if (codecSettings != null) {
            intent.putExtra(EXTRA_CONFIGURATION, JSONObject(codecSettings).toString())
        }
        return intent
    }

    fun createUpdateStunServersIntent(callbackId: Int, accountId: Int, stunServerList: List<String>, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_UPDATE_STUN_SERVERS
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId)
        intent.putStringArrayListExtra(EXTRA_STUN_SERVER_LIST, ArrayList(stunServerList))
        return intent
    }

    fun createChangeNetworkConfigurationIntent(callbackId: Int, configuration: Map<String, Any>?, context: Context): Intent {
        val intent = Intent(context, PjSipService::class.java)
        intent.action = ACTION_CHANGE_NETWORK_CONFIGURATION
        intent.putExtra(EXTRA_CALLBACK_ID, callbackId)
        if (configuration != null) {
            intent.putExtra(EXTRA_CONFIGURATION, JSONObject(configuration).toString())
        }
        return intent
    }
} 