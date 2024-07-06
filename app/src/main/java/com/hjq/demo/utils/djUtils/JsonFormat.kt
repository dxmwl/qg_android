package com.hjq.demo.utils.djUtils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

/**
 * create by hanweiwei on 9/5/23
 */
object JsonFormat {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun format(obj: Any?): String {
        obj ?: return ""
        try {
            return gson.toJson(obj)
        } catch (e: Exception) {
        }
        return ""
    }

    fun format(json: String?): String {
        json ?: return ""
        try {
            return gson.toJson(JsonParser().parse(json))
        } catch (e: Exception) {
        }
        return ""
    }

}