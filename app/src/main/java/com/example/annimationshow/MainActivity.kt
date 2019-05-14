package com.example.annimationshow

import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.annimationshow.adapter.InterpolatorAdapter
import java.text.FieldPosition


class MainActivity : AppCompatActivity() {

    //列表数据源
    lateinit var interpolatorLists:MutableList<DataBean>
    lateinit var adapter: InterpolatorAdapter

    lateinit var currentInterpolator:TimeInterpolator



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            it.add(DataBean(true,"SpringInterpolator",AccelerateDecelerateInterpolator()))
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

    }

    private fun onInterpolatorChecked(position: Int){
        currentInterpolator = interpolatorLists[position].polator
    }

    private fun
}
