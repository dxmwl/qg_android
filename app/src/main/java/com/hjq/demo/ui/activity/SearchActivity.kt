package com.hjq.demo.ui.activity

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.hjq.demo.R
import com.hjq.demo.aop.SingleClick
import com.hjq.demo.app.AppActivity
import com.hjq.shape.view.ShapeEditText

/**
 * @ClassName: SearchActivity
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/14 0014 12:06
 **/
class SearchActivity : AppActivity() {

    private val input_content: ShapeEditText? by lazy { findViewById(R.id.input_content) }
    private var keyWord = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        setOnClickListener(R.id.btn_search)

        input_content?.addTextChangedListener {
            keyWord = it.toString()
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
                SearchResultActivity.start(this@SearchActivity, keyWord)
            }

            else -> {}
        }
    }
}