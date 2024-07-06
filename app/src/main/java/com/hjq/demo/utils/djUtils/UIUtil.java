package com.hjq.demo.utils.djUtils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Create by hanweiwei on 18/03/2020.
 */
public final class UIUtil {
    private static float sDensity = -1f;
    private static int sDensityDpi = -1;
    private static float sScaledDensity = -1f;
    private static int sScreenWidthPixels = -1;
    private static int sScreenHeightPixels = -1;
    private static int sStatusBarHeight = -1;//状态栏高度
    private static int sBottomGestureHeight = -1; //底部手势区域高度


    private static boolean checkDisplayNull() {
        return sDensity < 0 || sDensityDpi < 0 || sScaledDensity < 0
                || sScreenWidthPixels < 0 || sScreenHeightPixels < 0;
    }

    private static void init(Context context) {
        Context ctx = context;
        if (ctx == null) {
            return;
        }
        if (checkDisplayNull()) {
            //2从context读取
            DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
            sDensity = dm.density;
            sDensityDpi = dm.densityDpi;
            sScaledDensity = dm.scaledDensity;
            sScreenWidthPixels = dm.widthPixels;
            sScreenHeightPixels = dm.heightPixels;
        }
        //防止宽高一成不变
        if (ctx.getResources() != null && ctx.getResources().getConfiguration() != null) {
            if (ctx.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                if (sScreenWidthPixels > sScreenHeightPixels) {
                    int temp = sScreenWidthPixels;
                    sScreenWidthPixels = sScreenHeightPixels;
                    sScreenHeightPixels = temp;
                }
            } else {
                if (sScreenWidthPixels < sScreenHeightPixels) {
                    int temp = sScreenWidthPixels;
                    sScreenWidthPixels = sScreenHeightPixels;
                    sScreenHeightPixels = temp;
                }
            }
        }
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / (scale == 0 ? 1 : scale) + 0.5f);
    }

    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / (fontScale == 0 ? 1 : fontScale) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        init(context);
        return sScreenWidthPixels;
    }

    public static int getScreenHeight(Context context) {
        init(context);
        return sScreenHeightPixels;
    }

    public static int getPortraitScreenWidth(Context context) {
        init(context);
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? sScreenWidthPixels : sScreenHeightPixels;
    }

    public static int getPortraitScreenHeight(Context context) {
        init(context);
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? sScreenHeightPixels : sScreenWidthPixels;
    }

    public static float getDensity(Context context) {
        init(context);
        return sDensity;
    }

    public static float getScaledDensity(Context context) {
        init(context);
        return sScaledDensity;
    }

    public static int getDensityDpi(Context context) {
        init(context);
        return sDensityDpi;
    }


    private static boolean visibilityValid(int visiable) {
        return visiable == View.VISIBLE || visiable == View.GONE || visiable == View.INVISIBLE;
    }

    public static void setViewVisibility(View v, int visiable) {
        if (v == null || v.getVisibility() == visiable || !visibilityValid(visiable)) {
            return;
        }
        v.setVisibility(visiable);
    }

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight > 0) {
            return sStatusBarHeight;
        }
        if (context == null) {
            return 0;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        //通过反射获取
        if (result <= 0) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                result = context.getResources().getDimensionPixelSize(height);
            } catch (Throwable ignore) {
            }
        }

        sStatusBarHeight = result;

        return result;
    }

    public static void forbidWebViewLongClick(WebView webView) {
        //禁止webview长按弹出复制提示框
        if (webView != null) {
            webView.setLongClickable(true);
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }

    public static int rgba2argb(String rgbaStr) {
        if (TextUtils.isEmpty(rgbaStr)) {
            return Color.WHITE;
        }

        int rgba = Color.parseColor(rgbaStr);
        if (rgbaStr.length() == 9) {
            return (rgba >>> 8) | (rgba << (32 - 8));
        }
        return rgba;
    }

    /**
     * 校准颜色值
     *
     * @param color 被检测的颜色值
     * @param value 如果颜色值不合法，使用 value 为默认值
     * @return
     */
    public static String calibrateColor(String color, String value) {
        try {
            Color.parseColor(color);
        } catch (Throwable ignore) {
            return value;
        }
        return color;
    }

    public static void setBottomGestureHeight(int height) {
        sBottomGestureHeight = height;
    }

    public static int getBottomGestureHeight() {
        return sBottomGestureHeight;
    }

    public static boolean canVerticalScroll(TextView textView) {
        int contentHeight = textView.getLayout().getHeight();
        int layoutHeight = textView.getHeight() - textView.getCompoundPaddingBottom() - textView.getCompoundPaddingTop();
        return textView.getScrollY() > 0 || textView.getScrollY() + layoutHeight < contentHeight;
    }
}
