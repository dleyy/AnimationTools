package com.example.annimationshow

import android.animation.TimeInterpolator
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.annimationshow.bean.DataBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var points: MutableList<PointF> = ArrayList()
//        points.add(PointF(0f, -0.1f))
//        points.add(PointF(0.1f, 0f))
//        points.add(PointF(0.2f, 0.1f))
//        points.add(PointF(0.3f, 0.2f))
//        points.add(PointF(0.4f, 0.3f))
//        points.add(PointF(0.5f, 0.4f))
//        points.add(PointF(0.6f, 0.6f))
        for (i in 0..10){
            points.add(PointF(i/10f,i/10f-0.1f))
        }

        tv_run.setOnClickListener {
            orbit_view.setPointFs(points)
        }
    }
}
