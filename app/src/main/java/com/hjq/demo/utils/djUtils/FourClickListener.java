package com.hjq.demo.utils.djUtils;

import android.os.SystemClock;
import android.view.View;


import java.util.Arrays;

public abstract class FourClickListener implements View.OnClickListener {
    private static final int DEFAULT_MAX_COUNT = 4;
    private final static long DEFAULT_DURATION = 2000;

    private long[] mHitRecord;

    @Override
    public void onClick(View v) {
        // 多次点击
        System.arraycopy(getHitRecord(), 1, getHitRecord(), 0, getHitRecord().length - 1);
        getHitRecord()[getHitRecord().length - 1] = SystemClock.uptimeMillis();
        // 多次点击判断
        if (getHitRecord()[0] >= (SystemClock.uptimeMillis() - getDuration())) {
            try {
                Arrays.fill(getHitRecord(), 0);
            } catch (Throwable ignore) {
            }
            onTimesClick(v);
        }
    }

    protected void onTimesClick(View v) {

    }

    private long[] getHitRecord() {
        if (mHitRecord == null) {
            mHitRecord = new long[getMaxCount()];
        }
        return mHitRecord;
    }

    protected int getMaxCount() {
        //连续点击次数，可以自定义，值必须>1
        return DEFAULT_MAX_COUNT;
    }

    protected long getDuration() {
        //连续点击有效时间，即在getDuration时长内必须点击getMaxCount次
        return DEFAULT_DURATION;
    }

}
