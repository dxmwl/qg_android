package com.hjq.demo.utils.djUtils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * create by hanweiwei on 8/30/23
 */
object PermissionsHelper {
    private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    fun request(activity: Activity) {
        val list = arrayListOf<String>()
        for (i in permissions.indices) {
            val per = permissions[i]
            if (ContextCompat.checkSelfPermission(activity, per) != PackageManager.PERMISSION_GRANTED) {
                list.add(per)
            }
        }

        if (list.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), 100)
        }
    }


}