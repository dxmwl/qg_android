package com.hjq.demo.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.DJXUpdate
import com.bytedance.sdk.djx.IDJXService
import com.gyf.immersionbar.ImmersionBar
import com.hjq.bar.TitleBar
import com.hjq.base.FragmentPagerAdapter
import com.hjq.demo.R
import com.hjq.demo.aop.SingleClick
import com.hjq.demo.app.AppFragment
import com.hjq.demo.app.TitleBarFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.activity.SearchActivity
import com.hjq.demo.ui.adapter.TabAdapter
import com.hjq.demo.ui.adapter.TabAdapter.Companion.TAB_MODE_SLIDING
import com.hjq.demo.ui.adapter.TabAdapter.OnTabListener
import com.orhanobut.logger.Logger

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页 Fragment
 */
class HomeFragment : TitleBarFragment<HomeActivity>(), OnTabListener,
    OnPageChangeListener {

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val tabView: RecyclerView? by lazy { findViewById(R.id.rv_home_tab) }
    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }
    private val toolbar: TitleBar? by lazy { findViewById(R.id.title_bar) }

    private var tabAdapter: TabAdapter? = null
    private var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>? = null

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {

        setOnClickListener(R.id.btn_search)

        pagerAdapter = FragmentPagerAdapter(this)
        viewPager?.adapter = pagerAdapter
        viewPager?.addOnPageChangeListener(this)
        tabAdapter = TabAdapter(getAttachActivity()!!, tabMode = TAB_MODE_SLIDING, fixed = false)
        tabView?.adapter = tabAdapter

        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), toolbar)
    }

    override fun initData() {
        val personRec = DJXUpdate.getPersonRec()
        Logger.d("获取短剧推荐状态:$personRec")
        DJXSdk.service().requestDramaCategoryList(object : IDJXService.IDJXDramaCategoryCallback {
            override fun onError(p0: Int, p1: String?) {
                Logger.d("获取短剧分类失败:${p0},${p1}")
            }

            override fun onSuccess(p0: MutableList<String>?) {
                Logger.d("获取短剧分类成功:$p0")
                tabAdapter?.addItem("推荐")
                pagerAdapter?.addFragment(DjListFragment.newInstance("推荐"), "推荐")
                p0?.forEach {
                    tabAdapter?.addItem(it)
                    pagerAdapter?.addFragment(DjListFragment.newInstance(it), it)

                }
            }

        })

        tabAdapter?.setOnTabListener(this)
    }

    /**
     * [TabAdapter.OnTabListener]
     */
    override fun onTabSelected(recyclerView: RecyclerView?, position: Int): Boolean {
        viewPager?.currentItem = position
        return true
    }

    /**
     * [ViewPager.OnPageChangeListener]
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        tabAdapter?.setSelectedPosition(position)
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        viewPager?.removeOnPageChangeListener(this)
        tabAdapter?.setOnTabListener(null)
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_search -> {
                startActivity(SearchActivity::class.java)
            }
            else -> {}
        }
    }
}