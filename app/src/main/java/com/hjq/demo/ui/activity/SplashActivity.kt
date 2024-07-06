package com.hjq.demo.ui.activity

import android.app.Application
import android.content.Intent
import android.view.*
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.blankj.utilcode.util.ScreenUtils
import com.bytedance.applog.AppLog
import com.bytedance.applog.InitConfig
import com.bytedance.applog.util.UriConstants
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.DJXSdkConfig
import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPSdkConfig
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.CSJAdError
import com.bytedance.sdk.openadsdk.CSJSplashAd
import com.bytedance.sdk.openadsdk.CSJSplashCloseType
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdNative.*
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfig
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hjq.demo.R
import com.hjq.demo.app.AppActivity
import com.hjq.demo.app.AppApplication
import com.hjq.demo.http.api.UserInfoApi
import com.hjq.demo.http.model.HttpData
import com.hjq.demo.other.AppConfig
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.widget.view.SlantedTextView
import com.orhanobut.logger.Logger
import com.hjq.demo.utils.djUtils.DemoConst
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 闪屏界面
 */
class SplashActivity : AppActivity() {

    private val lottieView: LottieAnimationView? by lazy { findViewById(R.id.lav_splash_lottie) }
    private val debugView: SlantedTextView? by lazy { findViewById(R.id.iv_splash_debug) }
    private val mSplashContainer: FrameLayout? by lazy { findViewById(R.id.ad_view) }

    //@[classname]
    private var csjSplashAdListener: TTAdNative.CSJSplashAdListener? = null

    //@[classname]
    private var splashAdListener: CSJSplashAd.SplashAdListener? = null

    //@[classname]
    private var csjSplashAd: CSJSplashAd? = null

    override fun getLayoutId(): Int {
        return R.layout.splash_activity
    }

    override fun initView() {
        // 设置动画监听
//        lottieView?.addAnimatorListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//                lottieView?.removeAnimatorListener(this)
//                HomeActivity.start(this@SplashActivity)
//                finish()
//            }
//        })

        initCsjApplog(application)
        initCsjSdk(application)
    }

    override fun initData() {
        debugView?.let {
            it.setText(AppConfig.getBuildType().uppercase(Locale.getDefault()))
            if (AppConfig.isDebug()) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }

        if (true) {
            return
        }
        // 刷新用户信息
        EasyHttp.post(this)
            .api(UserInfoApi())
            .request(object : HttpCallback<HttpData<UserInfoApi.Bean?>>(this) {

                override fun onSucceed(data: HttpData<UserInfoApi.Bean?>) {

                }
            })
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
            // 隐藏状态栏和导航栏
            .hideBar(BarHide.FLAG_HIDE_BAR)
    }

    override fun onBackPressed() {
        // 禁用返回键
        //super.onBackPressed();
    }

    override fun initActivity() {
        // 问题及方案：https://www.cnblogs.com/net168/p/5722752.html
        // 如果当前 Activity 不是任务栈中的第一个 Activity
        if (!isTaskRoot) {
            val intent: Intent? = intent
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (((intent != null) && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                        && (Intent.ACTION_MAIN == intent.action))
            ) {
                // 对当前 Activity 执行销毁操作，避免重复实例化入口
                finish()
                return
            }
        }
        super.initActivity()
    }

    private fun jumpActivity() {
//        if (UserManager.tokenResult == null) {
//            startActivity(LoginActivity::class.java)
//        } else {
        HomeActivity.start(this@SplashActivity)
//        }
        finish()
    }

    /**
     * 初始化穿山甲
     */
    private fun initCsjSdk(application: Application?) {
        val configJsonStr =
            "{\"cypher\":2,\"message\":\"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\"}"
        var configJsonObj: JSONObject? = null
        try {
            configJsonObj = JSONObject(configJsonStr)
        } catch (e: JSONException) {
            Logger.e("初始化穿山甲默认配置json失败")
        }

        val sdkVersion = TTAdSdk.getAdManager().getSDKVersion()
        Logger.d("SDK_VERSION:${sdkVersion}")
        TTAdSdk.init(
            application, TTAdConfig.Builder()
                .appId("5465945")
                .useMediation(true)//开启聚合功能，默认false
                .debug(AppConfig.isDebug())
                .setMediationConfig(
                    MediationConfig.Builder()
                        .setCustomLocalConfig(configJsonObj)
                        .build()
                )
                .build()
        )
        TTAdSdk.start(object : TTAdSdk.Callback {
            override fun success() {
                Logger.d("CSJAD_success:" + TTAdSdk.isSdkReady())

                //1. 初始化，最好放到application.onCreate()执行
                val configBuilder = DPSdkConfig.Builder()
                    .debug(AppConfig.isDebug())
                DPSdk.init(
                    AppApplication.getApplication(),
                    DemoConst.SDK_SETTINGS_CONFIG,
                    configBuilder.build()
                )
                DPSdk.start { isSuccess, message ->
                    //请确保使用Sdk时Sdk已经成功启动
                    //isSuccess=true表示启动成功
                    //启动失败，可以再次调用启动接口（建议最多不要超过3次)
//                    isDPStarted = isSuccess
                    Logger.e("短视频start result=$isSuccess, msg=$message")
//                    Bus.getInstance().sendEvent(DPStartEvent(isSuccess))
                    loadAd()
                }
                val config = DJXSdkConfig.Builder().debug(AppConfig.isDebug()).build()
                // 初始化，最好放到application.onCreate()执行
                DJXSdk.init(AppApplication.getApplication(), DemoConst.SDK_SETTINGS_CONFIG, config)
                //需要在广告SDK初始化成功后启动短剧服务
                DJXSdk.start { isSuccess, message ->
                    // ...
                    Logger.e("短剧start result=$isSuccess, msg=$message")
                }
            }

            override fun fail(p0: Int, p1: String?) {
                Logger.d("CSJAD_fail:${p0}, ${p1}")
                jumpActivity()
            }
        })
    }

    fun loadAd() {
        val appScreenWidth = ScreenUtils.getScreenWidth()
        val appScreenHeight = ScreenUtils.getScreenHeight()
        Logger.d("屏幕宽高：${appScreenWidth}, ${appScreenHeight}")
        /** 1、创建AdSlot对象 */
        //@[classname]
        val adslot = AdSlot.Builder()
            .setCodeId("102635847")
            .setImageAcceptedSize(
                appScreenWidth,
                appScreenHeight
            )
            .build()

        /** 2、创建TTAdNative对象 */
        //@[classname]//@[methodname]
        val adNativeLoader = TTAdSdk.getAdManager().createAdNative(this@SplashActivity)

        /** 3、创建加载、展示监听器 */
        initListeners()

        /** 4、加载广告  */
        adNativeLoader.loadSplashAd(adslot, csjSplashAdListener, 3500)
    }

    //@[classname]
    fun showAd(csjSplashAd: CSJSplashAd?) {
        /** 5、渲染成功后，展示广告 */
        this.csjSplashAd = csjSplashAd
        csjSplashAd?.setSplashAdListener(splashAdListener)
        csjSplashAd?.let {
            it.splashView?.let { splashView ->
                mSplashContainer?.addView(splashView)
            }
        }
    }

    private fun initListeners() {
        // 广告加载监听器
        //@[classname]
        csjSplashAdListener = object : CSJSplashAdListener {
            //@[classname]
            override fun onSplashLoadSuccess(ad: CSJSplashAd?) {
                Logger.d("onSplashAdLoad")
            }

            //@[classname]
            override fun onSplashLoadFail(error: CSJAdError?) {
                Logger.d("onError code = ${error?.code} msg = ${error?.msg}")
                jumpActivity()
            }

            //@[classname]
            override fun onSplashRenderSuccess(ad: CSJSplashAd?) {
                Logger.d("onSplashRenderSuccess")
                showAd(ad)
            }

            //@[classname]
            override fun onSplashRenderFail(ad: CSJSplashAd?, error: CSJAdError?) {
                Logger.d("onError code = ${error?.code} msg = ${error?.msg}")
                jumpActivity()
            }
        }
        //@[classname]
        splashAdListener = object : CSJSplashAd.SplashAdListener {
            //@[classname]
            override fun onSplashAdShow(p0: CSJSplashAd?) {
                Logger.d("onSplashAdShow")
            }

            //@[classname]
            override fun onSplashAdClick(p0: CSJSplashAd?) {
                Logger.d("onSplashAdClick")
            }

            //@[classname]
            override fun onSplashAdClose(p0: CSJSplashAd?, closeType: Int) {
                //@[classname]
                if (closeType == CSJSplashCloseType.CLICK_SKIP) {
                    Logger.d("开屏广告点击跳过")
                    //@[classname]
                } else if (closeType == CSJSplashCloseType.COUNT_DOWN_OVER) {
                    Logger.d("开屏广告点击倒计时结束")
                    //@[classname]
                } else if (closeType == CSJSplashCloseType.CLICK_JUMP) {
                    Logger.d("点击跳转")
                }
                jumpActivity()
            }
        }
    }

    /**
     * 初始化穿山甲视频
     */
    private fun initCsjApplog(application: Application?) {

        /* 初始化开始，appid和渠道，appid如不清楚请联系客户成功经理
        * 注意第二个参数 channel 不能为空
        */
        val config = InitConfig("576882", AppConfig.getChannel())
        //上报地址
        config.setUriConfig(UriConstants.DEFAULT)
        // 加密开关，SDK 5.5.1 及以上版本支持，false 为关闭加密，上线前建议设置为 true
        AppLog.setEncryptAndCompress(true)

        config.setAutoStart(true)
        /* 初始化结束 */
        config.setAutoStart(true)

        AppLog.init(this, config)
    }

    override fun onDestroy() {
        super.onDestroy()
        /** 6、在onDestroy中销毁广告  */
        csjSplashAd?.mediationManager?.destroy()
    }
}