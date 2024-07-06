package com.hjq.demo.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaUnlockListener
import com.bytedance.sdk.djx.model.DJXDrama
import com.bytedance.sdk.djx.model.DJXDramaDetailConfig
import com.bytedance.sdk.djx.model.DJXDramaUnlockAdMode
import com.bytedance.sdk.djx.model.DJXDramaUnlockInfo
import com.bytedance.sdk.djx.model.DJXDramaUnlockMethod
import com.bytedance.sdk.djx.params.DJXWidgetDramaDetailParams
import com.hjq.demo.R
import com.hjq.demo.aop.Log
import com.hjq.demo.app.AppActivity
import com.hjq.demo.ui.dialog.UnlockDialog
import com.hjq.demo.ui.dialog.VipDialog
import com.hjq.demo.ui.fragment.DjListFragment
import com.orhanobut.logger.Logger

/**
 * @ClassName: DjPlayActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/05 0005 18:17
 **/
class DjPlayActivity : AppActivity() {

    companion object {

        private const val DRAMA_ID: String = "DRAMA_ID"
        private const val VIEW_INDEX: String = "VIEW_INDEX"

        @Log
        fun start(context: Context, dramaId: Long, viewIndex: Int = 1) {
            val intent = Intent(context, DjPlayActivity::class.java)
            intent.putExtra(DRAMA_ID, dramaId)
            intent.putExtra(VIEW_INDEX, viewIndex)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val blockView: LinearLayout? by lazy { findViewById(R.id.block_view) }

    private lateinit var detailConfig: DJXDramaDetailConfig

    //短剧ID
    private var dramaId: Long = 0
    //看到第几集
    private var viewIndex: Int = 1

    //免费集数
    private val freeCount = 6

    //每次解锁集数
    private val lockSet = 5

    override fun getLayoutId(): Int {
        return R.layout.activity_dj_play
    }

    override fun initView() {

        dramaId = getLong(DRAMA_ID)
        viewIndex = getInt(VIEW_INDEX)

        initConfig()

        val dramaDetail = DJXSdk.factory().createDramaDetail(
            DJXWidgetDramaDetailParams.obtain(dramaId, viewIndex, detailConfig)
                .currentDuration(0)
                .fromGid("-1") // 必传，否则影响推荐效果
                .from(DJXWidgetDramaDetailParams.DJXDramaEnterFrom.DEFAULT) // 必传，否则影响推荐效果
        ).fragment

        supportFragmentManager.beginTransaction().replace(R.id.fl_container, dramaDetail).commit()

    }

    private fun initConfig() {
        detailConfig = DJXDramaDetailConfig.obtain(
            DJXDramaUnlockAdMode.MODE_COMMON,
            freeCount,
            object : IDJXDramaUnlockListener {
                override fun unlockFlowEnd(
                    drama: DJXDrama,
                    errCode: IDJXDramaUnlockListener.UnlockErrorStatus?,
                    map: Map<String, Any>?
                ) {
                    Logger.d("unlockFlowEnd: $drama, code: $errCode, map: $map")
                    if (errCode == null) {
                        blockView?.visibility = View.GONE
                    }
                }

                override fun unlockFlowStart(
                    drama: DJXDrama,
                    callback: IDJXDramaUnlockListener.UnlockCallback,
                    map: Map<String, Any>?
                ) {
                    // 解锁支持多种方式：支付、广告。根据业务需求自行定义
                    // 如果在其他时机已经购买会员可以设置 unlockInfo 中的 hasMember 为 true
                    // val info = DJXDramaUnlockInfo(drama.id, lockSet, DJXDramaUnlockMethod.METHOD_PAY_MEMBER, true)
                    // callback.onConfirm(info)

                    UnlockDialog(this@DjPlayActivity).apply {
                        setListener(
                            vip = {
                                //点击购买会员
                                //这里也可以先查询是否有会员：DJXSdk.service().queryPayVips()，然后在弹窗购买会员
                                VipDialog(this@DjPlayActivity)
                                    .setPayType(1)
                                    .setListener(object : VipDialog.IVipListener {
                                        override fun onPay(orderParams: String) {
                                            val info = DJXDramaUnlockInfo(
                                                drama.id,
                                                lockSet,
                                                DJXDramaUnlockMethod.METHOD_PAY_MEMBER,
                                                false,
                                                orderParams
                                            )
                                            callback.onConfirm(info)
                                        }
                                    })
                                    .show()
                            },
                            money = {
                                //点击单部剧购买
                                VipDialog(this@DjPlayActivity)
                                    .setPayType(2)
                                    .setDramaId(drama.id)
                                    .setListener(object : VipDialog.IVipListener {
                                        override fun onPay(orderParams: String) {
                                            val info = DJXDramaUnlockInfo(
                                                drama.id,
                                                lockSet,
                                                DJXDramaUnlockMethod.METHOD_PAY_SKIT,
                                                false,
                                                orderParams
                                            )
                                            callback.onConfirm(info)
                                        }
                                    })
                                    .show()
                            },
                            ad = {
                                //点击激励视频解锁
                                val info = DJXDramaUnlockInfo(
                                    drama.id,
                                    lockSet,
                                    DJXDramaUnlockMethod.METHOD_AD,
                                    false
                                )
                                callback.onConfirm(info)
                            },
                            close = {
                                hide()
                                val info = DJXDramaUnlockInfo(
                                    drama.id,
                                    lockSet,
                                    DJXDramaUnlockMethod.METHOD_AD,
                                    cancelUnlock = true
                                )
                                callback.onConfirm(info)
                            }
                        )
                        show()
                    }
                }

            })
    }

    override fun initData() {

    }
}