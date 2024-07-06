package com.hjq.demo.utils.djUtils

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.DJXSdkConfig
import com.bytedance.sdk.djx.IDJXPrivacyController
import com.bytedance.sdk.djx.IDJXWidget
import com.bytedance.sdk.djx.interfaces.listener.IDJXAdListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaHomeListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDrawListener
import com.bytedance.sdk.djx.model.DJXDramaDetailConfig
import com.bytedance.sdk.djx.model.DJXDramaUnlockAdMode
import com.bytedance.sdk.djx.params.DJXWidgetDramaHomeParams
import com.bytedance.sdk.djx.params.DJXWidgetDrawParams
import com.hjq.demo.utils.djUtils.DemoConst.SDK_SETTINGS_CONFIG

/**
 * create by hanweiwei on 8/30/23
 */
object DJXHolder {
    private const val TAG = "DJXHolder"

    const val FREE_SET = 5
    const val LOCK_SET = 2

    //初始化
    fun init(application: Application, callback: ((success: Boolean) -> Unit)?) {
        doInitTask(application, callback)
    }

    private fun doInitTask(application: Application, callback: ((success: Boolean) -> Unit)?) {
        val config = DJXSdkConfig.Builder()
                .debug(true)
                .newUser(true)
                .build()

        // 配置青少年模式，可选
        config.privacyController = object : IDJXPrivacyController() {
            override fun isTeenagerMode(): Boolean {
                return false
            }
        }

        // 配置隐私控制开关，可选
        //config.privacyController()

        //注入路由功能
        config.router = DJXRouterImpl()

        DJXSdk.init(application, SDK_SETTINGS_CONFIG, config)
        DJXSdk.start { isSuccess, message ->
            Log.d(TAG, "doInitTask: $isSuccess, $message")
            callback?.invoke(isSuccess)

            val msg = if (isSuccess) "初始化成功" else "初始化失败：$message"
            Toast.makeText(application, msg, Toast.LENGTH_LONG).show()
        }
    }

    //加载短剧
    fun loadDramaDraw(dramaListener: IDJXDramaListener?, drawListener: IDJXDrawListener?, adListener: IDJXAdListener?): IDJXWidget {
        val dramaDetailConfig = DJXDramaDetailConfig.obtain(DJXDramaUnlockAdMode.MODE_COMMON, FREE_SET, DefaultDramaUnlockListener(
            LOCK_SET, null)
        )
                .listener(DefaultDramaListener(dramaListener)) // 短剧详情页视频播放回调
                .adListener(adListener) // 短剧详情页激励视频回调

        return DJXSdk.factory().createDraw(
                DJXWidgetDrawParams.obtain()
                        .adOffset(0) //单位 dp，为 0 时可以不设置
                        .drawContentType(DJXWidgetDrawParams.DRAW_CONTENT_TYPE_ONLY_DRAMA)
                        .drawChannelType(DJXWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND)
                        .hideClose(true, null)
                        .hideChannelName(true)
                        .detailConfig(dramaDetailConfig)
                        .listener(DefaultDrawListener(drawListener)) // 混排流内视频监听
                        .adListener(DefaultAdListener(adListener)) // 混排流内广告监听
        )
    }

    //加载剧场首页
    fun loadDramaHome(listener: IDJXDramaHomeListener?): IDJXWidget {
        val detailConfig = DJXDramaDetailConfig.obtain(DJXDramaUnlockAdMode.MODE_COMMON, FREE_SET, DefaultDramaUnlockListener(
            LOCK_SET, null)
        )
        val params = DJXWidgetDramaHomeParams.obtain(detailConfig)
                .showBackBtn(false)
                .listener(DefaultDramaHomeListener(listener))

        return DJXSdk.factory().createDramaHome(params)
    }

}