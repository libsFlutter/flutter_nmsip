package org.tele.flutter_sip2

import org.json.JSONObject
import org.tele.flutter_sip2.dto.CallSettingsDTO

class PjSipCall(
    private val account: PjSipAccount,
    private val destination: String,
    private val callSettings: CallSettingsDTO
) {
    val id: Int = generateId()
    val callId: String = generateCallId()
    val accountId: Int = account.id
    val localContact: String? = null
    val localUri: String = account.uri
    val remoteContact: String? = null
    val remoteUri: String = "sip:$destination@${account.domain}"
    val state: String = "PJSIP_INV_STATE_CALLING"
    val stateText: String = "Calling"
    val held: Boolean = false
    val muted: Boolean = false
    val speaker: Boolean = false
    val connectDuration: Int = -1
    val totalDuration: Int = 0
    val remoteOfferer: Boolean = false
    val remoteAudioCount: Int = 0
    val remoteVideoCount: Int = 0
    val audioCount: Int = 1
    val videoCount: Int = 0
    val lastStatusCode: Int? = null
    val lastReason: String? = null
    val media: Map<String, Any>? = null
    val provisionalMedia: Map<String, Any>? = null

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("callId", callId)
        json.put("accountId", accountId)
        json.put("localContact", localContact)
        json.put("localUri", localUri)
        json.put("remoteContact", remoteContact)
        json.put("remoteUri", remoteUri)
        json.put("state", state)
        json.put("stateText", stateText)
        json.put("held", held)
        json.put("muted", muted)
        json.put("speaker", speaker)
        json.put("connectDuration", connectDuration)
        json.put("totalDuration", totalDuration)
        json.put("remoteOfferer", remoteOfferer)
        json.put("remoteAudioCount", remoteAudioCount)
        json.put("remoteVideoCount", remoteVideoCount)
        json.put("audioCount", audioCount)
        json.put("videoCount", videoCount)
        json.put("lastStatusCode", lastStatusCode)
        json.put("lastReason", lastReason)
        json.put("media", media)
        json.put("provisionalMedia", provisionalMedia)
        return json
    }

    private fun generateId(): Int {
        return System.currentTimeMillis().toInt()
    }

    private fun generateCallId(): String {
        return "call_${System.currentTimeMillis()}"
    }
} 