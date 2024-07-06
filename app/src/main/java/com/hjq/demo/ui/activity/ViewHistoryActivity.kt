package com.hjq.demo.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.IDJXService
import com.bytedance.sdk.djx.model.DJXDrama
import com.hjq.demo.R
import com.hjq.demo.action.StatusAction
import com.hjq.demo.app.AppActivity
import com.hjq.demo.ui.adapter.ViewHistoryAdapter
import com.hjq.demo.widget.StatusLayout
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @ClassName: ViewHistoryActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/14 0014 13:22
 **/
class ViewHistoryActivity : AppActivity(), StatusAction {

    private lateinit var viewHistoryAdapter: ViewHistoryAdapter
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }
    private val dj_list: RecyclerView? by lazy { findViewById(R.id.dj_list) }
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_view_history
    }

    override fun initView() {

        dj_list?.also {
            it.layoutManager =
                LinearLayoutManager(this@ViewHistoryActivity)
            viewHistoryAdapter = ViewHistoryAdapter(this@ViewHistoryActivity)
            it.adapter = viewHistoryAdapter
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getViewHistory()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            getViewHistory()
        }
    }

    override fun initData() {
        getViewHistory()
    }

    private fun getViewHistory() {
        DJXSdk.service().getDramaHistory(pageNum, 20, object :
            IDJXService.IDJXDramaCallback {
            override fun onError(p0: Int, p1: String?) {
                toast(p1)
                Logger.d("获取短剧列表失败:${p0},${p1}")
                refresh?.finishRefresh()
                refresh?.finishLoadMore()
            }

            override fun onSuccess(p0: MutableList<out DJXDrama>?, p1: MutableMap<String, Any>?) {
                Logger.d("获取短剧列表成功:${p0},${p1}")
                refresh?.finishRefresh()
                refresh?.finishLoadMore()
                if (pageNum == 1) {
                    viewHistoryAdapter.clearData()
                }
                viewHistoryAdapter.addData(p0 as MutableList<DJXDrama>)
                if (viewHistoryAdapter.getCount() == 0) {
                    showEmpty()
                } else {
                    showComplete()
                }
            }

        })
    }

    override fun getStatusLayout(): StatusLayout? {
        return status_layout
    }
}