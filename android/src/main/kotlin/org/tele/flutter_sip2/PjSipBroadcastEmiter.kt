package org.tele.flutter_sip2

import android.content.Context
import android.content.Intent
import android.util.Log
import org.json.JSONObject

class PjSipBroadcastEmiter(private val context: Context) {
    
    fun emmit(callbackId: Int, successful: Boolean, data: String?) {
        val intent = Intent("pjSipCallback")
        intent.putExtra("callback_id", callbackId)
        intent.putExtra("successful", successful)
        intent.putExtra("data", data)
        context.sendBroadcast(intent)
    }

    fun emmitRegistrationChanged(account: PjSipAccount) {
        val intent = Intent("pjSipRegistrationChanged")
        intent.putExtra("data", account.toJson().toString())
        context.sendBroadcast(intent)
    }

    fun emmitCallReceived(account: PjSipAccount, call: PjSipCall) {
        val intent = Intent("pjSipCallReceived")
        val data = JSONObject().apply {
            put("account", account.toJson())
            put("call", call.toJson())
        }
        intent.putExtra("data", data.toString())
        context.sendBroadcast(intent)
    }

    fun emmitCallChanged(call: PjSipCall) {
        val intent = Intent("pjSipCallChanged")
        intent.putExtra("data", call.toJson().toString())
        context.sendBroadcast(intent)
    }

    fun emmitCallTerminated(call: PjSipCall) {
        val intent = Intent("pjSipCallTerminated")
        intent.putExtra("data", call.toJson().toString())
        context.sendBroadcast(intent)
    }

    fun emmitCallScreenLocked(lock: Boolean) {
        val intent = Intent("pjSipCallScreenLocked")
        intent.putExtra("lock", lock)
        context.sendBroadcast(intent)
    }

    fun emmitMessageReceived(account: PjSipAccount, message: PjSipMessage) {
        val intent = Intent("pjSipMessageReceived")
        val data = JSONObject().apply {
            put("account", account.toJson())
            put("message", message.toJson())
        }
        intent.putExtra("data", data.toString())
        context.sendBroadcast(intent)
    }

    fun emmitConnectivityChanged(available: Boolean) {
        val intent = Intent("pjSipConnectivityChanged")
        intent.putExtra("available", available)
        context.sendBroadcast(intent)
    }
} 