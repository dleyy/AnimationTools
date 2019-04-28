package com.example.annimationshow.util

import android.content.Context

fun dp2px(context: Context,dp:Int):Float{
    var density = context.resources.displayMetrics.density
    return density*dp+0.5f
}