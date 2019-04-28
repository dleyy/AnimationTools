package com.example.annimationshow

import android.animation.TimeInterpolator
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.annimationshow.bean.DataBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var points:MutableList<PointF> = ArrayList()
        for (i in 0..10 step 1){
            points.add(PointF(i/10f,i/10f))
        }
        tv_run.setOnClickListener {
            orbit_view.setPointFs(points)
        }
    }
}
