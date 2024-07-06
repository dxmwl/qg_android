package com.hjq.demo.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bytedance.sdk.djx.model.DJXDrama
import com.hjq.demo.R
import com.hjq.demo.app.AppAdapter
import com.hjq.demo.ui.activity.DjPlayActivity
import com.hjq.demo.widget.RoundImageView
import com.hjq.shape.view.ShapeTextView

/**
 * @ClassName: ViewHistoryAdapter
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/14 0014 13:26
 **/
class ViewHistoryAdapter (val mContext: Context) : AppAdapter<DJXDrama>(mContext) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AppViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_view_history) {

        private val djThumb: RoundImageView? by lazy { findViewById(R.id.dj_thumb) }
        private val label_name: ShapeTextView? by lazy { findViewById(R.id.label_name) }
        private val textView2: TextView? by lazy { findViewById(R.id.textView2) }
        private val textView3: TextView? by lazy { findViewById(R.id.textView3) }
        private val textView5: TextView? by lazy { findViewById(R.id.textView5) }
        private val play_num: TextView? by lazy { findViewById(R.id.play_num) }
        private val view_count: TextView? by lazy { findViewById(R.id.view_count) }

        override fun onBindView(position: Int) {
            val item = getItem(position)

            djThumb?.let { Glide.with(mContext).load(item.coverImage).into(it) }
            play_num?.text = item.favoriteCount.toString()
            view_count?.text = "已看到第${item.index}集"
            val jiangeTime = System.currentTimeMillis() / 1000 - item.createTime
            //大于三天不显示新剧标志
            label_name?.visibility =
                if (jiangeTime > 60 * 60 * 24 * 3) View.GONE else View.VISIBLE
            textView2?.text = item.type
            textView3?.text = "${item.total}集"
            textView5?.text = item.title

            getItemView().setOnClickListener {
                DjPlayActivity.start(mContext, item.id,item.index)
            }
        }
    }
}