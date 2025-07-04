package org.tele.flutter_sip2.dto

import org.json.JSONObject

data class ServiceConfigurationDTO(
    var userAgent: String = "",
    var stunServers: List<String> = emptyList()
) {
    fun isUserAgentNotEmpty(): Boolean = userAgent.isNotEmpty()
    
    fun isStunServersNotEmpty(): Boolean = stunServers.isNotEmpty()
    
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("userAgent", userAgent)
        json.put("stunServers", stunServers)
        return json
    }
    
    companion object {
        fun fromJson(json: JSONObject): ServiceConfigurationDTO {
            return ServiceConfigurationDTO(
                userAgent = json.optString("userAgent", ""),
                stunServers = json.optJSONArray("stunServers")?.let { array ->
                    (0 until array.length()).map { array.getString(it) }
                } ?: emptyList()
            )
        }
    }
} 