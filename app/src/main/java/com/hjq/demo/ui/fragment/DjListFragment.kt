package com.hjq.demo.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.IDJXService
import com.bytedance.sdk.djx.model.DJXDrama
import com.hjq.demo.R
import com.hjq.demo.aop.Log
import com.hjq.demo.app.AppFragment
import com.hjq.demo.other.GridSpaceDecoration
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.DjListAdapter
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @ClassName: DjListFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/05 0005 17:21
 **/
class DjListFragment : AppFragment<HomeActivity>() {

    companion object {

        private const val CATEGORY_NAME: String = "categoryName"

        @Log
        fun newInstance(categoryName: String): DjListFragment {
            val fragment = DjListFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY_NAME, categoryName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var djListAdapter: DjListAdapter
    private val recyclerView: RecyclerView? by lazy { findViewById(R.id.recyclerview) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }

    private var categoryName: String? = ""
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_dj_list
    }

    override fun initView() {
        categoryName = getString(CATEGORY_NAME)
        recyclerView?.also {
            it.layoutManager = GridLayoutManager(getAttachActivity(), 3)
            djListAdapter = DjListAdapter(requireContext())
            it.adapter = djListAdapter
            it.addItemDecoration(GridSpaceDecoration(resources.getDimension(R.dimen.dp_10).toInt()))
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            getDjList()
        }

        refresh?.setOnLoadMoreListener {
            pageNum++
            getDjList()
        }
    }

    override fun initData() {
        getDjList()
    }

    private fun getDjList() {
        if (categoryName == "推荐") {
            getRecommendDj()
        } else {
            getCategoryDj()
        }
    }

    private fun getCategoryDj() {
        DJXSdk.service().requestDramaByCategory(
            categoryName,
            pageNum,
            20,
            object : IDJXService.IDJXDramaCallback {
                override fun onError(p0: Int, p1: String?) {
                    Logger.d("获取短剧列表失败:${p0},${p1}")
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                }

                override fun onSuccess(
                    p0: MutableList<out DJXDrama>?,
                    p1: MutableMap<String, Any>?
                ) {
                    Logger.d("获取短剧列表成功:${p0},${p1}")
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageNum == 1) {
                        djListAdapter.clearData()
                    }
                    djListAdapter.addData(p0 as MutableList<DJXDrama>)
                }
            })
    }

    private fun getRecommendDj() {
        DJXSdk.service()
            .requestAllDramaByRecommend(pageNum, 20, object : IDJXService.IDJXDramaCallback {
                override fun onError(p0: Int, p1: String?) {
                    Logger.d("获取推荐短剧列表失败:${p0},${p1}")
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                }

                override fun onSuccess(
                    p0: MutableList<out DJXDrama>?,
                    p1: MutableMap<String, Any>?
                ) {
                    Logger.d("获取推荐短剧列表成功:${p0},${p1}")
                    refresh?.finishRefresh()
                    refresh?.finishLoadMore()
                    if (pageNum == 1) {
                        djListAdapter.clearData()
                    }
                    djListAdapter.addData(p0 as MutableList<DJXDrama>)
                }
            })
    }
}