package org.tele.flutter_sip2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.flutter.plugin.common.EventChannel
import org.json.JSONObject

class PjSipBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private var eventSink: EventChannel.EventSink? = null
        private val callbacks = mutableMapOf<Int, io.flutter.plugin.common.MethodChannel.Result>()
        private var callbackIdCounter = 0

        fun register(result: io.flutter.plugin.common.MethodChannel.Result): Int {
            val callbackId = callbackIdCounter++
            callbacks[callbackId] = result
            return callbackId
        }

        fun setEventSink(sink: EventChannel.EventSink?) {
            eventSink = sink
        }

        fun getFilter(): IntentFilter {
            val filter = IntentFilter()
            filter.addAction("pjSipRegistrationChanged")
            filter.addAction("pjSipCallReceived")
            filter.addAction("pjSipCallChanged")
            filter.addAction("pjSipCallTerminated")
            filter.addAction("pjSipCallScreenLocked")
            filter.addAction("pjSipMessageReceived")
            filter.addAction("pjSipConnectivityChanged")
            filter.addAction("pjSipCallback")
            return filter
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "pjSipCallback" -> {
                val callbackId = intent.getIntExtra("callback_id", -1)
                val successful = intent.getBooleanExtra("successful", false)
                val data = intent.getStringExtra("data")

                val callback = callbacks.remove(callbackId)
                if (callback != null) {
                    if (successful) {
                        if (data != null) {
                            try {
                                val jsonData = JSONObject(data)
                                val resultMap = jsonToMap(jsonData)
                                callback.success(resultMap)
                            } catch (e: Exception) {
                                callback.success(data)
                            }
                        } else {
                            callback.success(null)
                        }
                    } else {
                        callback.error("SIP_ERROR", data, null)
                    }
                }
            }
            "pjSipRegistrationChanged" -> {
                val data = intent.getStringExtra("data")
                if (data != null && eventSink != null) {
                    try {
                        val jsonData = JSONObject(data)
                        val eventMap = mapOf(
                            "type" to "registration_changed",
                            "data" to jsonToMap(jsonData)
                        )
                        eventSink?.success(eventMap)
                    } catch (e: Exception) {
                        eventSink?.error("SIP_EVENT_ERROR", "Failed to parse registration event", null)
                    }
                }
            }
            "pjSipCallReceived" -> {
                val data = intent.getStringExtra("data")
                if (data != null && eventSink != null) {
                    try {
                        val jsonData = JSONObject(data)
                        val eventMap = mapOf(
                            "type" to "call_received",
                            "data" to jsonToMap(jsonData)
                        )
                        eventSink?.success(eventMap)
                    } catch (e: Exception) {
                        eventSink?.error("SIP_EVENT_ERROR", "Failed to parse call received event", null)
                    }
                }
            }
            "pjSipCallChanged" -> {
                val data = intent.getStringExtra("data")
                if (data != null && eventSink != null) {
                    try {
                        val jsonData = JSONObject(data)
                        val eventMap = mapOf(
                            "type" to "call_changed",
                            "data" to jsonToMap(jsonData)
                        )
                        eventSink?.success(eventMap)
                    } catch (e: Exception) {
                        eventSink?.error("SIP_EVENT_ERROR", "Failed to parse call changed event", null)
                    }
                }
            }
            "pjSipCallTerminated" -> {
                val data = intent.getStringExtra("data")
                if (data != null && eventSink != null) {
                    try {
                        val jsonData = JSONObject(data)
                        val eventMap = mapOf(
                            "type" to "call_terminated",
                            "data" to jsonToMap(jsonData)
                        )
                        eventSink?.success(eventMap)
                    } catch (e: Exception) {
                        eventSink?.error("SIP_EVENT_ERROR", "Failed to parse call terminated event", null)
                    }
                }
            }
            "pjSipCallScreenLocked" -> {
                val lock = intent.getBooleanExtra("lock", false)
                if (eventSink != null) {
                    val eventMap = mapOf(
                        "type" to "call_screen_locked",
                        "data" to mapOf("lock" to lock)
                    )
                    eventSink?.success(eventMap)
                }
            }
            "pjSipMessageReceived" -> {
                val data = intent.getStringExtra("data")
                if (data != null && eventSink != null) {
                    try {
                        val jsonData = JSONObject(data)
                        val eventMap = mapOf(
                            "type" to "message_received",
                            "data" to jsonToMap(jsonData)
                        )
                        eventSink?.success(eventMap)
                    } catch (e: Exception) {
                        eventSink?.error("SIP_EVENT_ERROR", "Failed to parse message received event", null)
                    }
                }
            }
            "pjSipConnectivityChanged" -> {
                val available = intent.getBooleanExtra("available", false)
                if (eventSink != null) {
                    val eventMap = mapOf(
                        "type" to "connectivity_changed",
                        "data" to mapOf("available" to available)
                    )
                    eventSink?.success(eventMap)
                }
            }
        }
    }

    private fun jsonToMap(jsonObject: JSONObject): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.get(key)
            when (value) {
                is JSONObject -> map[key] = jsonToMap(value)
                is org.json.JSONArray -> map[key] = jsonArrayToList(value)
                else -> map[key] = value
            }
        }
        return map
    }

    private fun jsonArrayToList(jsonArray: org.json.JSONArray): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until jsonArray.length()) {
            val value = jsonArray.get(i)
            when (value) {
                is JSONObject -> list.add(jsonToMap(value))
                is org.json.JSONArray -> list.add(jsonArrayToList(value))
                else -> list.add(value)
            }
        }
        return list
    }
} 