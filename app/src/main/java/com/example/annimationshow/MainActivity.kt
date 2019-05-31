package com.example.annimationshow

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.*
import com.example.annimationshow.bean.DataBean
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.annimationshow.adapter.InterpolatorAdapter
import android.animation.AnimatorSet
import android.util.Log
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.example.annimationshow.util.dp2px
import com.example.annimationshow.util.getScreenHeight
import com.example.annimationshow.util.getStatusHeight


class MainActivity : AppCompatActivity() {

    //将数据分为1000份
    private val perPoint = 1000

    //列表数据源
    private lateinit var interpolatorLists: MutableList<DataBean>
    lateinit var adapter: InterpolatorAdapter

    lateinit var currentInterpolator: TimeInterpolator
    lateinit var pointFList: MutableList<PointF>

    lateinit var viewAnimation: ObjectAnimator
    lateinit var floatAnimation: ValueAnimator
    lateinit var animatorSet: AnimatorSet
    var animationIsRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointFList = ArrayList<PointF>()

        createAdapterData()

        initAdapter()

        initAnimation()

        tv_run.setOnClickListener {
            if (!animationIsRunning) {
                startAnimator()
            }
        }
    }

    /**
     * 动画开始
     */
    private fun startAnimator() {
        viewAnimation.interpolator = currentInterpolator
        floatAnimation.interpolator = currentInterpolator

        animatorSet.duration = 2000L
        animatorSet.start()
    }

    /**
     * 初始化 view 动画 与 值动画
     */
    private fun initAnimation() {

        val start = dp2px(this, 35)
        val end = getScreenHeight(this) - dp2px(this, 85) -
                getStatusHeight(this)

        viewAnimation = ObjectAnimator.ofFloat(anim_view, "y", start, end)
        floatAnimation = ValueAnimator.ofFloat(0f, 1f)

        animatorSet = AnimatorSet()
        animatorSet.play(viewAnimation).with(floatAnimation)

        floatAnimation.addUpdateListener {
            val x = it.animatedFraction
            val y = currentInterpolator.getInterpolation(x)
            val pointF = PointF(x, y)
            orbit_view.setBigRedCirclePosition(pointF)
        }

        viewAnimation.addListener {
            it.doOnStart {
                animationIsRunning = true
            }
            it.doOnEnd {
                animationIsRunning = false
            }
        }

    }

    /**
     * 初始化适配器与 recycleView
     */
    private fun initAdapter() {
        adapter = InterpolatorAdapter(interpolatorLists)
        adapter.checkedFunc = this::onInterpolatorChecked

        recycle_view.layoutManager = LinearLayoutManager(this)
        recycle_view.adapter = adapter
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
