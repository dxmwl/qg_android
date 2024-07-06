package com.hjq.demo.utils.djUtils

import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.djx.interfaces.listener.IDJXAdListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaCardListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaHomeListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDramaUnlockListener
import com.bytedance.sdk.djx.interfaces.listener.IDJXDrawListener
import com.bytedance.sdk.djx.model.DJXDrama
import com.bytedance.sdk.djx.model.DJXDramaUnlockInfo
import com.bytedance.sdk.djx.model.DJXDramaUnlockMethod

class DefaultDramaListener(private val listener: IDJXDramaListener? = null) : IDJXDramaListener() {
    override fun onDJXSeekTo(position: Int, time: Long) {
        super.onDJXSeekTo(position, time)
        DemoLog.d("onDJXSeekTo: $position, $time")
        listener?.onDJXSeekTo(position, time)
    }

    override fun onDJXPageChange(position: Int, map: MutableMap<String, Any>?) {
        super.onDJXPageChange(position, map)
        DemoLog.d("onDJXPageChange: $position, $map")
        listener?.onDJXPageChange(position, map)
    }

    override fun onDJXVideoPlay(map: MutableMap<String, Any>?) {
        super.onDJXVideoPlay(map)
        DemoLog.d("onDJXVideoPlay: $map")
        listener?.onDJXVideoPlay(map)
    }

    override fun onDJXVideoPause(map: MutableMap<String, Any>?) {
        super.onDJXVideoPause(map)
        DemoLog.d("onDJXVideoPause: $map")
        listener?.onDJXVideoPause(map)
    }

    override fun onDJXVideoContinue(map: MutableMap<String, Any>?) {
        super.onDJXVideoContinue(map)
        DemoLog.d("onDJXVideoContinue: $map")
        listener?.onDJXVideoContinue(map)
    }

    override fun onDJXVideoCompletion(map: MutableMap<String, Any>?) {
        super.onDJXVideoCompletion(map)
        DemoLog.d("onDJXVideoCompletion: $map")
        listener?.onDJXVideoCompletion(map)
    }

    override fun onDJXVideoOver(map: MutableMap<String, Any>?) {
        super.onDJXVideoOver(map)
        DemoLog.d("onDJXVideoOver: $map")
        listener?.onDJXVideoOver(map)
    }

    override fun onDJXClose() {
        super.onDJXClose()
        DemoLog.d("onDJXClose: ")
        listener?.onDJXClose()
    }

    override fun onDJXRequestStart(map: MutableMap<String, Any>?) {
        super.onDJXRequestStart(map)
        DemoLog.d("onDJXRequestStart: $map")
        listener?.onDJXRequestStart(map)
    }

    override fun onDJXRequestFail(code: Int, msg: String?, map: MutableMap<String, Any>?) {
        super.onDJXRequestFail(code, msg, map)
        DemoLog.d("onDJXRequestFail: $code, $msg, $map")
        listener?.onDJXRequestFail(code, msg, map)
    }

    override fun onDJXRequestSuccess(list: MutableList<MutableMap<String, Any>>?) {
        super.onDJXRequestSuccess(list)
        DemoLog.d("onDJXRequestSuccess: $list")
        listener?.onDJXRequestSuccess(list)
    }

    override fun onDramaSwitch(map: MutableMap<String, Any>?) {
        super.onDramaSwitch(map)
        DemoLog.d("onDramaSwitch: $map")
        listener?.onDramaSwitch(map)
    }

    override fun onDramaGalleryShow(map: MutableMap<String, Any>?) {
        super.onDramaGalleryShow(map)
        DemoLog.d("onDramaGalleryShow: $map")
        listener?.onDramaGalleryShow(map)
    }

    override fun onDramaGalleryClick(map: MutableMap<String, Any>?) {
        super.onDramaGalleryClick(map)
        DemoLog.d("onDramaGalleryClick: $map")
        listener?.onDramaGalleryClick(map)
    }

    override fun onRewardDialogShow(map: MutableMap<String, Any>?) {
        super.onRewardDialogShow(map)
        DemoLog.d("onRewardDialogShow: $map")
        listener?.onRewardDialogShow(map)
    }

    override fun onUnlockDialogAction(action: String?, map: MutableMap<String, Any>?) {
        super.onUnlockDialogAction(action, map)
        DemoLog.d("onUnlockDialogAction: $action, $map")
        listener?.onUnlockDialogAction(action, map)
    }

    override fun onDurationChange(current: Long) {
        super.onDurationChange(current)
//        DemoLog.d("onDurationChange: $current")
        listener?.onDurationChange(current)
    }

    override fun createCustomView(container: ViewGroup?, map: MutableMap<String, Any>?): View? {
        DemoLog.d("createCustomView: $map")
        return listener?.createCustomView(container, map) ?: super.createCustomView(container, map)
    }
}

class DefaultDrawListener(private val listener: IDJXDrawListener? = null) : IDJXDrawListener() {
    override fun onDJXRefreshFinish() {
        super.onDJXRefreshFinish()
        DemoLog.d("onDJXRefreshFinish: ")
        listener?.onDJXRefreshFinish()
    }

    override fun onDJXSeekTo(position: Int, time: Long) {
        super.onDJXSeekTo(position, time)
        DemoLog.d("onDJXSeekTo: $position, $time")
        listener?.onDJXSeekTo(position, time)
    }

    override fun onDJXPageChange(position: Int, map: MutableMap<String, Any>?) {
        super.onDJXPageChange(position, map)
        DemoLog.d("onDJXPageChange: $position, $map")
        listener?.onDJXPageChange(position, map)
    }

    override fun onDJXVideoPlay(map: MutableMap<String, Any>?) {
        super.onDJXVideoPlay(map)
        DemoLog.d("onDJXVideoPlay: $map")
        listener?.onDJXVideoPlay(map)
    }

    override fun onDJXVideoPause(map: MutableMap<String, Any>?) {
        super.onDJXVideoPause(map)
        DemoLog.d("onDJXVideoPause: $map")
        listener?.onDJXVideoPause(map)
    }

    override fun onDJXVideoContinue(map: MutableMap<String, Any>?) {
        super.onDJXVideoContinue(map)
        DemoLog.d("onDJXVideoContinue: $map")
        listener?.onDJXVideoContinue(map)
    }

    override fun onDJXVideoCompletion(map: MutableMap<String, Any>?) {
        super.onDJXVideoCompletion(map)
        DemoLog.d("onDJXVideoCompletion: $map")
        listener?.onDJXVideoCompletion(map)
    }

    override fun onDJXVideoOver(map: MutableMap<String, Any>?) {
        super.onDJXVideoOver(map)
        DemoLog.d("onDJXVideoOver: $map")
        listener?.onDJXVideoOver(map)
    }

    override fun onDJXClose() {
        super.onDJXClose()
        DemoLog.d("onDJXClose: ")
        listener?.onDJXClose()
    }

    override fun onDJXReportResult(isSucceed: Boolean, map: MutableMap<String, Any>?) {
        super.onDJXReportResult(isSucceed, map)
        DemoLog.d("onDJXReportResult: $isSucceed, $map")
        listener?.onDJXReportResult(isSucceed, map)
    }

    override fun onDJXRequestStart(map: MutableMap<String, Any>?) {
        super.onDJXRequestStart(map)
        DemoLog.d("onDJXRequestStart: $map")
        listener?.onDJXRequestStart(map)
    }

    override fun onDJXRequestFail(code: Int, msg: String?, map: MutableMap<String, Any>?) {
        super.onDJXRequestFail(code, msg, map)
        DemoLog.d("onDJXRequestFail: $code, $msg, $map")
        listener?.onDJXRequestFail(code, msg, map)
    }

    override fun onDJXRequestSuccess(list: MutableList<MutableMap<String, Any>>?) {
        super.onDJXRequestSuccess(list)
        DemoLog.d("onDJXRequestSuccess: $list")
        listener?.onDJXRequestSuccess(list)
    }

    override fun onChannelTabChange(channel: Int) {
        super.onChannelTabChange(channel)
        DemoLog.d("onChannelTabChange: $channel")
        listener?.onChannelTabChange(channel)
    }

    override fun onDurationChange(current: Long) {
        super.onDurationChange(current)
//        DemoLog.d("onDurationChange: $current")
        listener?.onDurationChange(current)
    }

    override fun createCustomView(container: ViewGroup?, map: MutableMap<String, Any>?): View? {
        DemoLog.d("createCustomView: $map")
        return listener?.createCustomView(container, map) ?: super.createCustomView(container, map)
    }
}

class DefaultAdListener(private val listener: IDJXAdListener? = null) : IDJXAdListener() {
    override fun onDJXAdRequest(map: MutableMap<String, Any>?) {
        super.onDJXAdRequest(map)
        DemoLog.d("onDJXAdRequest: $map")
        listener?.onDJXAdRequest(map)
    }

    override fun onDJXAdRequestSuccess(map: MutableMap<String, Any>?) {
        super.onDJXAdRequestSuccess(map)
        DemoLog.d("onDJXAdRequestSuccess: $map")
        listener?.onDJXAdRequestSuccess(map)
    }

    override fun onDJXAdRequestFail(code: Int, msg: String?, map: MutableMap<String, Any>?) {
        super.onDJXAdRequestFail(code, msg, map)
        DemoLog.d("onDJXAdRequestFail: $code, $msg, $map")
        listener?.onDJXAdRequestFail(code, msg, map)
    }

    override fun onDJXAdFillFail(map: MutableMap<String, Any>?) {
        super.onDJXAdFillFail(map)
        DemoLog.d("onDJXAdFillFail: $map")
        listener?.onDJXAdFillFail(map)
    }

    override fun onDJXAdShow(map: MutableMap<String, Any>?) {
        super.onDJXAdShow(map)
        DemoLog.d("onDJXAdShow: $map")
        listener?.onDJXAdShow(map)
    }

    override fun onDJXAdPlayStart(map: MutableMap<String, Any>?) {
        super.onDJXAdPlayStart(map)
        DemoLog.d("onDJXAdPlayStart: $map")
        listener?.onDJXAdPlayStart(map)
    }

    override fun onDJXAdPlayPause(map: MutableMap<String, Any>?) {
        super.onDJXAdPlayPause(map)
        DemoLog.d("onDJXAdPlayPause: $map")
        listener?.onDJXAdPlayPause(map)
    }

    override fun onDJXAdPlayContinue(map: MutableMap<String, Any>?) {
        super.onDJXAdPlayContinue(map)
        DemoLog.d("onDJXAdPlayContinue: $map")
        listener?.onDJXAdPlayContinue(map)
    }

    override fun onDJXAdPlayComplete(map: MutableMap<String, Any>?) {
        super.onDJXAdPlayComplete(map)
        DemoLog.d("onDJXAdPlayComplete: $map")
        listener?.onDJXAdPlayComplete(map)
    }

    override fun onDJXAdClicked(map: MutableMap<String, Any>?) {
        super.onDJXAdClicked(map)
        DemoLog.d("onDJXAdClicked: $map")
        listener?.onDJXAdClicked(map)
    }

    override fun onRewardVerify(map: MutableMap<String, Any>?) {
        super.onRewardVerify(map)
        DemoLog.d("onRewardVerify: $map")
        listener?.onRewardVerify(map)
    }

    override fun onSkippedVideo(map: MutableMap<String, Any>?) {
        super.onSkippedVideo(map)
        DemoLog.d("onSkippedVideo: $map")
        listener?.onSkippedVideo(map)
    }
}

class DefaultDramaHomeListener(private val listener: IDJXDramaHomeListener? = null) : IDJXDramaHomeListener() {
    override fun onItemClick(drama: DJXDrama?, map: MutableMap<String, Any>?) {
        super.onItemClick(drama, map)
        DemoLog.d("onItemClick: $drama, $map")
        listener?.onItemClick(drama, map)
    }
}

class DefaultDramaCardListener(private val listener: IDJXDramaCardListener? = null) : IDJXDramaCardListener() {
    override fun onDJXVideoPlay(map: MutableMap<String, Any>?) {
        super.onDJXVideoPlay(map)
        DemoLog.d("onDJXVideoPlay:" + map?.toString())
        listener?.onDJXVideoPlay(map)
    }

    override fun onDJXVideoPause(map: MutableMap<String, Any>?) {
        super.onDJXVideoPause(map)
        DemoLog.d("onDJXVideoPause:" + map?.toString())
    }

    override fun onDJXVideoContinue(map: MutableMap<String, Any>?) {
        super.onDJXVideoContinue(map)
        DemoLog.d("onDJXVideoContinue:" + map?.toString())
    }

    override fun onDJXVideoCompletion(map: MutableMap<String, Any>?) {
        super.onDJXVideoCompletion(map)
        DemoLog.d("onDJXVideoCompletion:" + map?.toString())
    }

    override fun onDJXVideoOver(map: MutableMap<String, Any>?) {
        super.onDJXVideoOver(map)
        DemoLog.d("onDJXVideoOver:" + map?.toString())
    }

    override fun onDJXRequestFail(code: Int, msg: String?, map: MutableMap<String, Any>?) {
        super.onDJXRequestFail(code, msg, map)
        DemoLog.d("onDJXRequestFail:" + map?.toString())
    }

    override fun onDJXRequestSuccess(list: MutableList<MutableMap<String, Any>>?) {
        super.onDJXRequestSuccess(list)
        list?.forEach {
            DemoLog.d("onDJXRequestSuccess: $it")
        }
    }
}

class DefaultDramaUnlockListener(private val lockSet: Int, private val listener: IDJXDramaUnlockListener? = null) : IDJXDramaUnlockListener {
    override fun unlockFlowStart(drama: DJXDrama, callback: IDJXDramaUnlockListener.UnlockCallback, map: Map<String, Any>?) {
        val unlockInfo = DJXDramaUnlockInfo(drama.id, lockSet, DJXDramaUnlockMethod.METHOD_AD, false)
        callback.onConfirm(unlockInfo)
        DemoLog.d("unlockFlowStart: $drama")
        listener?.unlockFlowStart(drama, callback, map)
    }

    override fun unlockFlowEnd(drama: DJXDrama, errCode: IDJXDramaUnlockListener.UnlockErrorStatus?, map: Map<String, Any>?) {
        DemoLog.d("unlockFlowEnd: $drama")
        listener?.unlockFlowEnd(drama, errCode, map)
    }
}