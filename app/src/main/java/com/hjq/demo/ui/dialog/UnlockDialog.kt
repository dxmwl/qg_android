package com.hjq.demo.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.hjq.demo.R

/**
 * @ClassName: UnlockDialog
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/4/05 0005 20:18
 **/
class UnlockDialog(context: Context) : Dialog(context, R.style.media_BottomDialog) {

    private val btnVip by lazy { findViewById<View>(R.id.btn_unlock_by_vip) }
    private val btnMoney by lazy { findViewById<View>(R.id.btn_unlock_by_money) }
    private val btnAd by lazy { findViewById<View>(R.id.btn_unlock_by_ad) }

    private var vipListener: View.OnClickListener? = null
    private var moneyListener: View.OnClickListener? = null
    private var adListener: View.OnClickListener? = null
    private var closeListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_unlock)
        setCanceledOnTouchOutside(false)

        findViewById<View>(R.id.iv_unlock_close)?.setOnClickListener {
            dismiss()
            closeListener?.onClick(it)
        }
        btnVip.setOnClickListener {
            dismiss()
            vipListener?.onClick(it)
        }
        btnMoney.setOnClickListener {
            dismiss()
            moneyListener?.onClick(it)
        }
        btnAd.setOnClickListener {
            dismiss()
            adListener?.onClick(it)
        }
    }

    fun setListener(vip: View.OnClickListener?, money: View.OnClickListener?, ad: View.OnClickListener?, close: View.OnClickListener?): UnlockDialog {
        vipListener = vip
        moneyListener = money
        adListener = ad
        closeListener = close
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
}