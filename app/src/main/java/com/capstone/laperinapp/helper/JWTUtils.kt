package com.capstone.laperinapp.helper

import org.json.JSONObject

class JWTUtils {
    companion object {
        private fun decoded(token: String): String {
            val split = token.split("\\.".toRegex()).toTypedArray()
            return getJson(split[1])
        }

        private fun getJson(strEncoded: String): String {
            val decodedBytes = android.util.Base64.decode(strEncoded, android.util.Base64.URL_SAFE)
            return String(decodedBytes)
        }

        fun getId(token: String): String {
            val decoded = decoded(token)
            val jsonObj = JSONObject(decoded)
            return jsonObj.getString("userId")
        }

        fun getUsername(token: String): String {
            val decoded = decoded(token)
            val jsonObj = JSONObject(decoded)
            return jsonObj.getString("username")
        }
    }
}