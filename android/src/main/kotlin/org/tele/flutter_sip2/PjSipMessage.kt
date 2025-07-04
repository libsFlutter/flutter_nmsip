package org.tele.flutter_sip2

import org.json.JSONObject

class PjSipMessage(
    val from: String,
    val to: String,
    val body: String,
    val contentType: String = "text/plain"
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("from", from)
        json.put("to", to)
        json.put("body", body)
        json.put("contentType", contentType)
        return json
    }
} 