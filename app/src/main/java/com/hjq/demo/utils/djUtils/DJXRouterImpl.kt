package com.hjq.demo.utils.djUtils

import com.blankj.utilcode.util.ActivityUtils
import com.bytedance.sdk.djx.IDJXRouter
import com.bytedance.sdk.djx.IDJXService

/**
 * create by hanweiwei on 10/9/23
 */
class DJXRouterImpl : IDJXRouter {

    override fun onLogin(callback: IDJXService.IDJXCallback<Boolean>?) {
//        if (!ActivityUtils.getTopActivity().isFinishing) {
//            LoginDialog(ActivityUtils.getTopActivity())
//                .setLoginListener(Runnable {
//                    //登录成功时一定要回调给短剧sdk，否则业务逻辑会异常
//                    callback?.onSuccess(true, null)
//                })
//                .show()
//        }
    }

}