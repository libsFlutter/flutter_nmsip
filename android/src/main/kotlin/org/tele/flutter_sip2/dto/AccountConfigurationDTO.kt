package org.tele.flutter_sip2.dto

import org.json.JSONObject

data class AccountConfigurationDTO(
    var name: String = "",
    var username: String = "",
    var domain: String = "",
    var password: String = "",
    var proxy: String? = null,
    var transport: String? = null,
    var regServer: String? = null,
    var regTimeout: Int = 3600,
    var regHeaders: Map<String, String> = emptyMap(),
    var regContactParams: String? = null
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("name", name)
        json.put("username", username)
        json.put("domain", domain)
        json.put("password", password)
        json.put("proxy", proxy)
        json.put("transport", transport)
        json.put("regServer", regServer)
        json.put("regTimeout", regTimeout)
        json.put("regContactParams", regContactParams)
        
        val headersJson = JSONObject()
        regHeaders.forEach { (key, value) ->
            headersJson.put(key, value)
        }
        json.put("regHeaders", headersJson)
        
        return json
    }
    
    companion object {
        fun fromJson(json: JSONObject): AccountConfigurationDTO {
            val headers = mutableMapOf<String, String>()
            json.optJSONObject("regHeaders")?.let { headersJson ->
                headersJson.keys().forEach { key ->
                    headers[key] = headersJson.getString(key)
                }
            }
            
            return AccountConfigurationDTO(
                name = json.optString("name", ""),
                username = json.optString("username", ""),
                domain = json.optString("domain", ""),
                password = json.optString("password", ""),
                proxy = json.optString("proxy"),
                transport = json.optString("transport"),
                regServer = json.optString("regServer"),
                regTimeout = json.optInt("regTimeout", 3600),
                regContactParams = json.optString("regContactParams"),
                regHeaders = headers
            )
        }
    }
} 