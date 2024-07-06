package com.hjq.demo.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.IDJXService
import com.bytedance.sdk.djx.model.DJXDrama
import com.hjq.base.BaseDialog
import com.hjq.demo.R
import com.hjq.demo.aop.SingleClick
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.*
import com.hjq.demo.ui.activity.ImageSelectActivity.OnPhotoSelectListener
import com.hjq.demo.ui.activity.VideoSelectActivity.OnVideoSelectListener
import com.hjq.demo.ui.activity.VideoSelectActivity.VideoBean
import com.hjq.demo.ui.adapter.ViewHistoryMineAdapter
import com.hjq.demo.ui.dialog.InputDialog
import com.hjq.demo.ui.dialog.MessageDialog
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import java.util.*

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 我的 Fragment
 */
class MineFragment : TitleBarFragment<HomeActivity>() {

    companion object {

        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    private lateinit var viewHistoryMineAdapter: ViewHistoryMineAdapter
    private val recyclerView: RecyclerView? by lazy { findViewById(R.id.recyclerView) }

    override fun getLayoutId(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        setOnClickListener(R.id.view_more_history)

        recyclerView?.also {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            viewHistoryMineAdapter = ViewHistoryMineAdapter(requireContext())
            it.adapter = viewHistoryMineAdapter
        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        getViewHistory()
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.view_more_history -> {
                startActivity(ViewHistoryActivity::class.java)
            }
        }
    }


    private fun getViewHistory() {
        DJXSdk.service().getDramaHistory(1, 20, object :
            IDJXService.IDJXDramaCallback {
            override fun onError(p0: Int, p1: String?) {
                toast(p1)
                Logger.d("获取短剧列表失败:${p0},${p1}")
            }

            override fun onSuccess(p0: MutableList<out DJXDrama>?, p1: MutableMap<String, Any>?) {
                Logger.d("获取短剧列表成功:${p0},${p1}")
                viewHistoryMineAdapter.setData(p0 as MutableList<DJXDrama>)
            }

        })
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }
}