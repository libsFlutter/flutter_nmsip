package org.tele.flutter_sip2

import org.json.JSONObject
import org.tele.flutter_sip2.dto.AccountConfigurationDTO

class PjSipAccount(private val configuration: AccountConfigurationDTO) {
    val id: Int = generateId()
    val uri: String = "sip:${configuration.username}@${configuration.domain}"
    val name: String = configuration.name
    val username: String = configuration.username
    val domain: String = configuration.domain
    val password: String = configuration.password
    val proxy: String? = configuration.proxy
    val transport: String? = configuration.transport
    val contactParams: String? = configuration.regContactParams
    val contactUriParams: String? = null
    val regServer: String = configuration.regServer ?: configuration.domain
    val regTimeout: Int = configuration.regTimeout
    val regContactParams: String? = configuration.regContactParams
    val regHeaders: Map<String, String> = configuration.regHeaders
    
    private val registration = AccountRegistration()

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("uri", uri)
        json.put("name", name)
        json.put("username", username)
        json.put("domain", domain)
        json.put("password", password)
        json.put("proxy", proxy)
        json.put("transport", transport)
        json.put("contactParams", contactParams)
        json.put("contactUriParams", contactUriParams)
        json.put("regServer", regServer)
        json.put("regTimeout", regTimeout)
        json.put("regContactParams", regContactParams)
        
        val headersJson = JSONObject()
        regHeaders.forEach { (key, value) ->
            headersJson.put(key, value)
        }
        json.put("regHeaders", headersJson)
        json.put("registration", registration.toJson())
        
        return json
    }

    private fun generateId(): Int {
        return System.currentTimeMillis().toInt()
    }

    inner class AccountRegistration {
        var status: Boolean = false
        var code: Int? = null
        var reason: String? = null
        var expiration: Int? = null
        var retryAfter: Int? = null

        fun toJson(): JSONObject {
            val json = JSONObject()
            json.put("status", status)
            json.put("code", code)
            json.put("reason", reason)
            json.put("expiration", expiration)
            json.put("retryAfter", retryAfter)
            return json
        }
    }
} 