package com.hjq.demo.utils.djUtils

import java.util.*

/**
 * create by hanweiwei on 10/7/23
 */
object SignUtil {
    //支付和登录需要的秘钥。注：这是demo的key，开发者有一定要替换成自己的
    const val appSecureKey = "828330c0854d93d4818a70e3ac0edd3e"

    fun nonce(): String {
        return random(16)
    }

    fun random(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuilder()
        for (i in 0 until length) {
            val number = random.nextInt(62)
            sb.append(str[number]);
        }
        return sb.toString();
    }

}