package com.example.annimationshow.util

import android.content.Context
import android.view.WindowManager
import android.util.DisplayMetrics
import android.util.Log


fun dp2px(context: Context,dp:Int):Float{
    var density = context.resources.displayMetrics.density
    return density*dp+0.5f
}

fun getScreenHeight(context: Context): Float {
    val metrics = DisplayMetrics()
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels.toFloat()
}

fun getStatusHeight(context: Context): Float {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result.toFloat()
}