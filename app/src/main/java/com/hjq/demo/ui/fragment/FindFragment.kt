package com.hjq.demo.ui.fragment

import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.model.DJXDramaDetailConfig
import com.bytedance.sdk.djx.model.DJXDramaUnlockAdMode
import com.bytedance.sdk.djx.params.DJXWidgetDramaDetailParams
import com.bytedance.sdk.djx.params.DJXWidgetDrawParams
import com.bytedance.sdk.djx.params.DJXWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND_THEATER
import com.bytedance.sdk.djx.params.DJXWidgetDrawParams.DRAW_CONTENT_TYPE_ONLY_DRAMA
import com.bytedance.sdk.dp.DPWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND
import com.hjq.demo.R
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.DjPlayActivity
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.utils.djUtils.DJXHolder.FREE_SET
import com.hjq.demo.utils.djUtils.DJXHolder.LOCK_SET
import com.hjq.demo.utils.djUtils.DefaultAdListener
import com.hjq.demo.utils.djUtils.DefaultDramaListener
import com.hjq.demo.utils.djUtils.DefaultDramaUnlockListener
import com.hjq.demo.utils.djUtils.DefaultDrawListener
import com.hjq.widget.view.SwitchButton
import com.orhanobut.logger.Logger


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
class FindFragment : TitleBarFragment<HomeActivity>(),
    SwitchButton.OnCheckedChangeListener {

    companion object {

        fun newInstance(): FindFragment {
            return FindFragment()
        }
    }

    private var mBottomOffset = -1 // 底部标题文案、进度条、评论按钮底部偏移

    private var mTitleTopMargin = -1 // 标题栏上边距

    private var mTitleLeftMargin = -1 // 标题栏左边间距

    private var mTitleRightMargin = -1 // 标题栏右边间距

    private var mDrawContentType = DRAW_CONTENT_TYPE_ONLY_DRAMA // draw流类型

    private var mDrawChannelType: Int = DRAW_CHANNEL_TYPE_RECOMMEND_THEATER // 沉浸式小视频频道，默认包含推荐和关注频道

    private var mIsHideChannelName = false // 是否隐藏频道名称

    private var mIsHideDramaInfo = false // 是否隐藏底部短剧信息

    private var mIsHideDramaEnter = false // 是否隐藏底部短剧入口


    override fun getLayoutId(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {
        val dramaDetailConfig =
            DJXDramaDetailConfig.obtain(
                DJXDramaUnlockAdMode.MODE_COMMON,
                FREE_SET,
                DefaultDramaUnlockListener(LOCK_SET, null)
            ).apply {
                listener(DefaultDramaListener(null))
                adListener(DefaultAdListener(null))
            }

        val dpWidget = DJXSdk.factory().createDraw(
            DJXWidgetDrawParams.obtain().apply {
                adOffset(0) //单位 dp，为 0 时可以不设置
                drawContentType(mDrawContentType)
                drawChannelType(mDrawChannelType)
                hideChannelName(mDrawChannelType == DJXWidgetDrawParams.DRAW_CHANNEL_TYPE_RECOMMEND)
                hideDramaInfo(mIsHideDramaInfo)
                hideDramaEnter(mIsHideDramaEnter)
//                dramaFree(dramaFree)
//                topDramaId(dramaTopId.toLong())
                hideClose(false, null)
                listener(DefaultDrawListener(null))
                adListener(DefaultAdListener(null))
                if (true) {
                    setEnterDelegate { context, drama, current ->
//                        DramaDetailActivity.outerDrama = drama
//                        DramaDetailActivity.enterFrom = enterFrom
//                        val intent = Intent(context, DramaDetailActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        intent.putExtra(DramaDetailConfigActivity.KEY_DRAMA_PLAY_DURATION, current)
//                        context.startActivity(intent)
                        DjPlayActivity.start(requireContext(), drama.id, drama.index)
                    }
                } else {
                    detailConfig(dramaDetailConfig)
                }
            }
        )
        childFragmentManager.beginTransaction().replace(R.id.fl_container, dpWidget.fragment)
            .commit()
    }

    override fun initData() {

    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    /**
     * [SwitchButton.OnCheckedChangeListener]
     */
    override fun onCheckedChanged(button: SwitchButton, checked: Boolean) {
        toast(checked)
    }
}