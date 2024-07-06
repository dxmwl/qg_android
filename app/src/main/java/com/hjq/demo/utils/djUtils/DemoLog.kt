package com.hjq.demo.utils.djUtils

import android.util.Log

/**
 * create by hanweiwei on 9/7/23
 */
object DemoLog {
    private const val TAG = "DEMO"

    fun v(msg: String?) {
        v("", msg)
    }

    fun v(tag: String, msg: String?) {
        Log.v(TAG + "_" + tag, msg ?: "")
    }

    fun d(msg: String?) {
        d("", msg)
    }

    fun d(tag: String, msg: String?) {
        Log.d(TAG + "_" + tag, msg ?: "")
    }

    fun i(msg: String?) {
        i("", msg)
    }

    fun i(tag: String, msg: String?) {
        Log.i(TAG + "_" + tag, msg ?: "")
    }

    fun e(msg: String?, e: Throwable? = null) {
        e("", msg, e)
    }

    fun e(tag: String, msg: String?, e: Throwable? = null) {
        Log.e(TAG + "_" + tag, msg ?: "", e)
    }

    fun w(msg: String?) {
        w("", msg)
    }

    fun w(tag: String, msg: String?) {
        Log.w(TAG + "_" + tag, msg ?: "")
    }

}