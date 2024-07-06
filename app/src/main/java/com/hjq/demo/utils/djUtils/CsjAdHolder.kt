package com.hjq.demo.utils.djUtils

import android.app.Application
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk

/**
 * create by hanweiwei on 8/30/23
 */
object CsjAdHolder {
    private const val TAG = "CsjAdHolder"

    //穿山甲sdk初始化
    fun init(siteId: String, application: Application, callback: TTAdSdk.Callback?) {
        val build = TTAdConfig.Builder()
            .appId(siteId) //穿山甲媒体id
            .useTextureView(true)
            .appName("djxsdk_demo")
            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
            .allowShowNotify(true)
            .supportMultiProcess(true)
            .debug(true)
            .build()
        TTAdSdk.init(application, build)
        TTAdSdk.start(object : TTAdSdk.Callback {
            override fun success() {
                callback?.success()
                Log.e(TAG, "TTAdSdk aysnc init success")
            }

            override fun fail(code: Int, msg: String?) {
                callback?.fail(code, msg)
                Log.e(TAG, "TTAdSdk aysnc init fail, code = $code msg = $msg")
            }
        })
    }

}