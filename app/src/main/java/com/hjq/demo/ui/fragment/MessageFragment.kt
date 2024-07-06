package com.hjq.demo.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPWidgetDrawParams
import com.bytedance.sdk.dp.IDPDrawListener
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.orhanobut.logger.Logger


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 消息 Fragment
 */
class MessageFragment : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): MessageFragment {
            return MessageFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.message_fragment
    }

    override fun initView() {
        initDrawWidget()
    }

    override fun initData() {}

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    private fun initDrawWidget() {
        val mIDPWidget = DPSdk.factory().createDraw(DPWidgetDrawParams.obtain()
            .adOffset(49) //单位 dp
            .hideClose(false, null)
            .listener(object : IDPDrawListener() {
                override fun onDPRefreshFinish() {
                    Logger.d("onDPRefreshFinish")
                }

                override fun onDPPageChange(position: Int) {
                    Logger.d("onDPPageChange: $position")
                }

                override fun onDPVideoPlay(map: Map<String, Any>) {
                    Logger.d("onDPVideoPlay")
                }

                override fun onDPVideoOver(map: Map<String, Any>) {
                    Logger.d("onDPVideoOver")
                }

                override fun onDPClose() {
                    Logger.d("onDPClose")
                }

                override fun onDPReportResult(isSucceed: Boolean) {
                    Logger.d("onDPReportResult")
                    if (isSucceed) {
                        toast("举报成功")
                    } else {
                        toast("举报失败，请稍后再试")
                    }
                }
            })
        )
        val fragment = mIDPWidget.fragment
        childFragmentManager.beginTransaction().replace(R.id.fl_container, fragment)
            .commit()
    }


}