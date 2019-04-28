package com.example.annimationshow

import android.animation.TimeInterpolator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.annimationshow.bean.DataBean

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var da = DataBean(true,"AccelerateDecelerateInterpolator",AccelerateDecelerateInterpolator())
    }
}
