package com.hjq.demo.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.IDJXService.IDJXDramaCallback
import com.bytedance.sdk.djx.model.DJXDrama
import com.hjq.demo.R
import com.hjq.demo.action.StatusAction
import com.hjq.demo.aop.Log
import com.hjq.demo.aop.SingleClick
import com.hjq.demo.app.AppActivity
import com.hjq.demo.other.GridSpaceDecoration
import com.hjq.demo.ui.adapter.DjListAdapter
import com.hjq.demo.widget.StatusLayout
import com.hjq.shape.view.ShapeEditText
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @ClassName: SearchResultActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/14 0014 12:56
 **/
class SearchResultActivity : AppActivity(), StatusAction {

    companion object {

        private const val KEY_WORD: String = "key_word"

        @Log
        fun start(context: Context, keyWord: String?) {
            val intent = Intent(context, SearchResultActivity::class.java)
            intent.putExtra(KEY_WORD, keyWord)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val input_content: ShapeEditText? by lazy { findViewById(R.id.input_content) }
    private val refresh: SmartRefreshLayout? by lazy { findViewById(R.id.refresh) }
    private val status_layout: StatusLayout? by lazy { findViewById(R.id.status_layout) }
    private val dj_list: RecyclerView? by lazy { findViewById(R.id.dj_list) }
    private lateinit var djListAdapter: DjListAdapter
    private var keyWord = ""
    private var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun initView() {
        keyWord = getString(KEY_WORD).toString()

        setOnClickListener(R.id.btn_search)

        input_content?.addTextChangedListener {
            searchDj()
        }

        input_content?.let {
            it.setText(keyWord)
            it.setSelection(keyWord.length)
        }

        dj_list?.also {
            it.layoutManager = GridLayoutManager(this@SearchResultActivity, 3)
            djListAdapter = DjListAdapter(this@SearchResultActivity)
            it.adapter = djListAdapter
            it.addItemDecoration(GridSpaceDecoration(resources.getDimension(R.dimen.dp_10).toInt()))
        }

        refresh?.setOnRefreshListener {
            pageNum = 1
            searchDj()
        }
        refresh?.setOnLoadMoreListener {
            pageNum++
            searchDj()
        }
    }

    override fun initData() {

    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                if (keyWord.isBlank()) {
                    toast("请输入要搜索的内容")
                    return
                }
                searchDj()
            }

            else -> {}
        }
    }

    private fun searchDj() {
        DJXSdk.service().searchDrama(keyWord, true, pageNum, 20, object : IDJXDramaCallback {
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
                    djListAdapter.clearData()
                }
                djListAdapter.addData(p0 as MutableList<DJXDrama>)
                if (djListAdapter.getCount() == 0) {
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