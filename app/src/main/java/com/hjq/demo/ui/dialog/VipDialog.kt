package com.hjq.demo.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.sdk.djx.DJXSdk
import com.bytedance.sdk.djx.IDJXService
import com.bytedance.sdk.djx.model.DJXCombo
import com.bytedance.sdk.djx.model.DJXOthers
import com.bytedance.sdk.djx.model.DJXProtocol
import com.hjq.demo.R
import com.hjq.demo.ui.activity.LoginActivity
import com.hjq.demo.utils.djUtils.DemoConst
import com.hjq.demo.utils.djUtils.SignUtil
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
/**
 * @ClassName: VipDialog
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/05 0005 20:22
 **/
class VipDialog(context: Context) : Dialog(context, R.style.media_BottomDialog) {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.rv_vip_goods) }
    private val btnPay by lazy { findViewById<View>(R.id.tv_vip_btn_pay) }
    private val checkAgree by lazy { findViewById<CheckBox>(R.id.rb_vip_agree) }
    private val tvAgree by lazy { findViewById<TextView>(R.id.tv_vip_agree_info) }

    private val adapter = VipAdapter()
    private val list = arrayListOf<DJXCombo>()
    private val signed = AtomicBoolean(false)
    private var payType = 0
    private var dramaId: Long? = null
    private var listener: IVipListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_vip)
        setCanceledOnTouchOutside(false)

        findViewById<View>(R.id.iv_vip_close)?.setOnClickListener { dismiss() }

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(
            context.resources.getDrawable(
                R.drawable.shape_vip_dialog_item_divider,
                null
            )
        )
        recyclerView.addItemDecoration(decoration)

        initPay()

        recyclerView.postDelayed(Runnable {
            initAgree()
            initRecycler()
        }, 50)
    }

    private fun initPay() {
        //调用sdk支付接口进行套餐购买
        btnPay?.setOnClickListener {
            if (!checkAgree.isChecked) {
                Toast.makeText(context, "协议未签署", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!DJXSdk.service().isLogin) {
                Toast.makeText(context, "未登录", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (list.isNullOrEmpty()) {
                Toast.makeText(context, "商品列表为空", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val combo = list[adapter.selectedIndex]

            //支付，这里主要有两步：1下单、2支付
            //1. 下单：订单参数由开发者服务器自行生成，demo这里直接使用
            val key = SignUtil.appSecureKey //这是demo的key，开发者有一定要替换成自己的
            val nonce = SignUtil.nonce()
            val time: Long = System.currentTimeMillis() / 1000
            val map = hashMapOf<String, String>().apply {
                this["combo_id"] = combo.id.toString()
//                this["order_no"] =
//                    "PAY_${DemoConst.SITE_ID}_${LoginActivity.getUid()}_${UUID.randomUUID()}"
                this["order_time"] = time.toString()
//                this["ouid"] = LoginActivity.getUid() ?: ""
                this["site_id"] = DemoConst.SITE_ID

                if (combo.isDramaType) {
                    this["shortplay_id"] = dramaId?.toString() ?: "1007"
                } else {
                    this["shortplay_id"] = "0"
                }
            }

            //生成加签的支付参数
            val orderParams = DJXSdk.service().getSignString(key, nonce, time, map)
            listener?.onPay(orderParams)

            //2. 支付
//            DJXSdk.service().pay(orderParams, object : IDJXService.IDJXCallback<DJXOrder> {
//                override fun onSuccess(data: DJXOrder?, others: DJXOthers?) {
//
//                }
//
//                override fun onError(code: Int, msg: String?) {
//
//                }
//            })
        }
    }

    private fun initAgree() {
        //获取支付协议并展示
        checkAgree.isChecked = false
        DJXSdk.service().getPayProtocol(object : IDJXService.IDJXCallback<List<DJXProtocol>> {
            override fun onSuccess(data: List<DJXProtocol>?, others: DJXOthers?) {
                if (data.isNullOrEmpty()) {
                    Toast.makeText(context, "协议请求为空", Toast.LENGTH_LONG).show()
                    return
                }

                val listIds = arrayListOf<String>()
                val builder = SpannableStringBuilder("我已阅读并同意")
                var allSign = true
                data.forEach {
                    allSign = allSign && it.isSigned
                    listIds.add(it.id.toString())
                    val span = SpannableString("《${it.name}》")
                    val click = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.data = Uri.parse(it.url)
                            context.startActivity(intent)
                        }
                    }
                    span.setSpan(click, 1, span.length - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                    builder.append(span)
                }

                signed.set(allSign)
                checkAgree.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        DJXSdk.service()
                            .signedPayProtocol(listIds, object : IDJXService.IDJXCallback<Boolean> {
                                override fun onSuccess(data: Boolean?, others: DJXOthers?) {
                                    Toast.makeText(
                                        context,
                                        if (data == true) "同意成功" else "同意失败",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    signed.set(data ?: false)
                                }

                                override fun onError(code: Int, msg: String?) {
                                    Toast.makeText(
                                        context,
                                        "同意失败：$code, $msg",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    signed.set(false)
                                }

                            })
                    }
                }

                tvAgree.setText(builder, TextView.BufferType.SPANNABLE)
                tvAgree.movementMethod = LinkMovementMethod.getInstance();
            }

            override fun onError(code: Int, msg: String?) {
                Toast.makeText(context, "协议请求失败", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initRecycler() {
        //查询所有套餐并展示
        val type = if (payType == 1 || payType == 2) payType.toString() else ""
        DJXSdk.service().getCombos(type, 0, object : IDJXService.IDJXCallback<List<DJXCombo>> {
            override fun onSuccess(data: List<DJXCombo>?, others: DJXOthers?) {
                data?.let { list.addAll(it) }
                adapter.notifyDataSetChanged()
            }

            override fun onError(code: Int, msg: String?) {
                Toast.makeText(context, "获取失败：$code, $msg", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setListener(listener: IVipListener?): VipDialog {
        this.listener = listener
        return this
    }

    //设置购买类型：0全部、1会员、2短剧
    fun setPayType(type: Int): VipDialog {
        this.payType = type
        return this
    }

    fun setDramaId(id: Long): VipDialog {
        this.dramaId = id
        return this
    }

    override fun show() {
        super.show()

        window?.let {
            it.decorView.setPadding(0, 0, 0, 0)
            it.setGravity(Gravity.CENTER)
            it.attributes?.apply {
                this.width = WindowManager.LayoutParams.MATCH_PARENT
                this.height = WindowManager.LayoutParams.WRAP_CONTENT
                window?.attributes = this
            }
        }
    }

    private inner class VipAdapter : RecyclerView.Adapter<VipHolder>() {
        var selectedIndex = 0

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VipHolder {
            val item = layoutInflater.inflate(R.layout.item_vip_good, viewGroup, false)
            return VipHolder(item)
        }

        override fun onBindViewHolder(holder: VipHolder, position: Int) {
            holder.setData(selectedIndex, position, list[position])
            holder.itemView.setOnClickListener {
                if (selectedIndex != position) {
                    selectedIndex = position
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    private inner class VipHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName by lazy { itemView.findViewById<TextView>(R.id.tv_item_vip_name) }
        val tvMoney by lazy { itemView.findViewById<TextView>(R.id.tv_item_vip_money) }
        val tvDesc by lazy { itemView.findViewById<TextView>(R.id.tv_item_vip_desc) }

        @SuppressLint("SetTextI18n")
        fun setData(selected: Int, position: Int, combo: DJXCombo) {
            if (selected == position) {
                //选中
                itemView.background = context.resources.getDrawable(
                    R.drawable.shape_vip_dialog_item_bg_selected,
                    null
                )
                tvName.setTextColor(Color.parseColor("#E7601F"))
                tvMoney.setTextColor(Color.parseColor("#E7601F"))
                tvDesc.setTextColor(Color.parseColor("#E7601F"))
            } else {
                itemView.background =
                    context.resources.getDrawable(R.drawable.shape_vip_dialog_item_bg, null)
                tvName.setTextColor(Color.parseColor("#000000"))
                tvMoney.setTextColor(Color.parseColor("#000000"))
                tvDesc.setTextColor(Color.parseColor("#000000"))
            }

            tvName.text = combo.name
            tvMoney.text = "￥${combo.payAmount / 100F}"
            tvDesc.text = combo.desc
        }
    }

    interface IVipListener {
        fun onPay(orderParams: String)
    }

}