package org.tele.flutter_sip2.dto

import org.json.JSONObject

data class CallSettingsDTO(
    var flag: Int = 0,
    var reqKeyframeMethod: Int = 0,
    var audCnt: Int = 1,
    var vidCnt: Int = 0
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("flag", flag)
        json.put("reqKeyframeMethod", reqKeyframeMethod)
        json.put("audCnt", audCnt)
        json.put("vidCnt", vidCnt)
        return json
    }
    
    companion object {
        fun fromJson(json: JSONObject): CallSettingsDTO {
            return CallSettingsDTO(
                flag = json.optInt("flag", 0),
                reqKeyframeMethod = json.optInt("reqKeyframeMethod", 0),
                audCnt = json.optInt("audCnt", 1),
                vidCnt = json.optInt("vidCnt", 0)
            )
        }
    }
} 