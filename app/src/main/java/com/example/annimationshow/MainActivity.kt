package com.example.annimationshow

import android.animation.TimeInterpolator
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.annimationshow.bean.DataBean
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.OvershootInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.annimationshow.adapter.InterpolatorAdapter


class MainActivity : AppCompatActivity() {

    //将数据分为1000份
    private val perPoint = 1000

    //列表数据源
    private lateinit var interpolatorLists: MutableList<DataBean>
    lateinit var adapter: InterpolatorAdapter

    lateinit var currentInterpolator: TimeInterpolator
    lateinit var pointFList: MutableList<PointF>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointFList = ArrayList()

        createAdapterData()

        adapter = InterpolatorAdapter(interpolatorLists)
        adapter.checkedFunc = this::onInterpolatorChecked

        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.adapter = adapter

        tv_run.setOnClickListener {
        }
    }

    /**
     * 创建列表数据源
     */
    private fun createAdapterData() {
        interpolatorLists = mutableListOf()
        interpolatorLists.let {
            it.add(DataBean(true, "AccelerateDecelerateInterpolator", AccelerateDecelerateInterpolator()))
            it.add(DataBean(false, "AccelerateInterpolator", AccelerateInterpolator()))
            it.add(DataBean(false, "AnticipateInterpolator", AnticipateInterpolator()))
            it.add(DataBean(false, "AnticipateOvershootInterpolator", AnticipateOvershootInterpolator()))
            it.add(DataBean(false, "BounceInterpolator", BounceInterpolator()))
            it.add(DataBean(false, "CycleInterpolator(1)", CycleInterpolator(1f)))
            it.add(DataBean(false, "DecelerateInterpolator", DecelerateInterpolator()))
            it.add(DataBean(false, "LinearInterpolator", LinearInterpolator()))
            it.add(DataBean(false, "OvershootInterpolator", OvershootInterpolator()))
        }
        currentInterpolator = interpolatorLists[0].polator
        buildPointF()
    }

    private fun onInterpolatorChecked(position: Int) {
        currentInterpolator = interpolatorLists[position].polator
        buildPointF()
    }

    /**
     * 构造数据点
     */
    private fun buildPointF() {
        pointFList.clear()
        for (i in 1..perPoint step 1) {
            var x = i.toFloat() / perPoint
            var y = currentInterpolator.getInterpolation(x)
            var pointF = PointF(x, y)
            pointFList.add(pointF)
        }
        orbit_view.setPointFs(pointFList)
    }

}
