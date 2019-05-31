package com.example.annimationshow.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.annimationshow.util.dp2px
import java.lang.Exception


class OrbitView(context: Context, att: AttributeSet?) : View(context, att) {

    //视图内边距
    private val viewPadding = dp2px(context, 20)

    //留给字体的偏移
    private val fontPadding = dp2px(context, 15)

    // 网格默认 宽高
    private val defaultGridWH = dp2px(context, 15)

    //默认坐标比例
    private val defaultRate = 10

    //字体大小
    private val fontSize = 16f

    //网线大小
    private val lineSize = 14f

    //刻度绘制起始点
    private val fontStartX = 0f

    //默认最高点
    private val defaultMaxPoint = PointF(0f, 1f)

    //默认最低点
    private val defaultMinPoint = PointF(0f, 0f)

    //默认 y 轴的网格数目
    private val defaultYCellNum = 10

    //默认 x 轴的网格数
    private val defaultXCellNum = 10

    //默认y上半轴的条目数
    private val defaultYPlusCount = 10

    //默认y下半轴的条目数
    private val defaultYMinusCount = 0

    //当前 y 轴的网格数目
    private var currentYCellNum = defaultYCellNum

    //当前 网格 宽高
    private var currentGridWH = defaultGridWH

    // 视图宽高
    private var mViewWidth = 0f
    private var mViewHeight = 0f

    //圆点坐标。
    private var circlePoint: PointF = PointF()

    private var yPlusCount = defaultYPlusCount

    private var yMinusCount = defaultYMinusCount

    //存储最远点坐标 --最高点的坐标
    private var endPointY: PointF = PointF()

    //开始点的坐标 --最底部的坐标
    private var startPointY = PointF()

    private var endPointX = PointF()

    //当前最低点
    private var minPoint = defaultMinPoint

    //当前最高点
    private var maxPoint = defaultMaxPoint

    //坐标轴画笔
    private var xyPaint: Paint = Paint()

    //轨迹画笔
    private var orbitPaint: Paint = Paint()

    //网格线画笔
    private var gridPaint: Paint = Paint()

    //坐标点集合
    private lateinit var points: MutableList<PointF>

    private var orbitPath = Path()
    constructor(context: Context) : this(context, null)

    constructor(context: Context, att: AttributeSet?, defAttr: Int) : this(context, att)

    init {
        initPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w.toFloat() - viewPadding
        mViewHeight = h.toFloat() - viewPadding

        calculateStartAndEndPoint()
    }

    /**
     * 设置值和点对应关系
     */
    fun setPointFs(list: MutableList<PointF>) {

        maxPoint = defaultMaxPoint
        minPoint = defaultMinPoint

        //计算最高点与最低点
        for (point in list) {
            if (point.y >= maxPoint.y) {
                maxPoint = point
            } else if (point.y <= minPoint.y) {
                minPoint = point
            }
        }
        this.points = list

        calculateStartAndEndPoint()

        /**
         * 如果buildPath 放这，去掉canvas 里面的话， 就不会显示
         * emmm,暂时不知道为什么. 2019/5/31
         */
        //buildPath()

        invalidate()
    }

    /**
     * 构建路径
     */
    private fun buildPath() {
        orbitPath.reset()
        if (points.isNotEmpty()) {
            orbitPath.moveTo(circlePoint.x,circlePoint.y)
            for (i in 0 until points.size step 1) {
                val currentPoint = points[i]
                val currentRate = currentPoint.y / (maxPoint.y - minPoint.y)
                val toCircleLY = currentRate * currentYCellNum * currentGridWH
                val toCircleLX = currentPoint.x * defaultXCellNum * currentGridWH

                when (i) {
                    0 -> orbitPath.moveTo(
                        circlePoint.x + toCircleLX,
                        circlePoint.y - toCircleLY
                    )
                    else -> orbitPath.lineTo(
                        circlePoint.x + toCircleLX,
                        circlePoint.y - toCircleLY
                    )
                }
            }
        }
    }

    /**
     * 计算 圆心 以及 结束点坐标。
     */
    private fun calculateStartAndEndPoint() {
        // 计算 y轴上半轴 以及 y轴下半轴的 网格 数目
        yPlusCount = (Math.abs(Math.ceil((maxPoint.y * defaultRate).toDouble()))).toInt()
        yMinusCount = (Math.abs(Math.floor((minPoint.y * defaultRate).toDouble()))).toInt()

        currentYCellNum = yPlusCount + yMinusCount
        currentGridWH = mViewHeight / currentYCellNum

        //计算圆点坐标
        circlePoint.x = fontPadding
        circlePoint.y = mViewHeight - yMinusCount * currentGridWH

        /**
         *  计算坐标轴上的起点与终点 坐标
         *  当只有一个象限的时候，起点即 圆点。否则起点就是最低点
         */
        if (yMinusCount == 0 || yPlusCount == 0) {
            endPointY.x = circlePoint.x
            startPointY.x = circlePoint.x

            if (yMinusCount == 0) {
                endPointY.y = circlePoint.y - yPlusCount * currentGridWH + yMinusCount * currentGridWH
                startPointY.y = circlePoint.y
            } else {
                endPointY.y = circlePoint.y
                startPointY.y = circlePoint.y - yPlusCount * currentGridWH + yMinusCount * currentGridWH
            }
        } else {
            startPointY.x = circlePoint.x
            startPointY.y = circlePoint.y + yMinusCount * currentGridWH

            endPointY.x = circlePoint.x
            endPointY.y = circlePoint.y - yPlusCount * currentGridWH
        }

        endPointX.x = defaultXCellNum * currentGridWH + circlePoint.x
        endPointX.y = circlePoint.y
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        xyPaint.let {
            it.isAntiAlias = true
            it.color = resources.getColor(android.R.color.holo_red_light)
            it.textSize = fontSize
            it.style = Paint.Style.FILL_AND_STROKE
        }

        gridPaint.let {
            it.isAntiAlias = true
            it.color = resources.getColor(android.R.color.darker_gray)
            it.textSize = lineSize
            it.style = Paint.Style.FILL_AND_STROKE
        }

        orbitPaint.let {
            it.isAntiAlias = true
            it.color = resources.getColor(android.R.color.holo_red_light)
            it.style = Paint.Style.STROKE
            it.strokeWidth = dp2px(context, 1)
        }
    }

    override fun onDraw(canvas: Canvas?) {

        buildPath()

        canvas?.let {
            //y轴
            it.drawLine(
                startPointY.x, startPointY.y,
                endPointY.x, endPointY.y, xyPaint
            )
            //x轴
            it.drawLine(
                circlePoint.x, circlePoint.y,
                endPointX.x, endPointX.y, xyPaint
            )

            //横向网格线
            for (i in 0..currentYCellNum step 1) {
                //不再重复绘制y轴
                if (circlePoint.y != startPointY.y - i * currentGridWH) {
                    it.drawLine(
                        circlePoint.x, startPointY.y - i * currentGridWH,
                        endPointX.x, startPointY.y - i * currentGridWH,
                        gridPaint
                    )
                }

                //最低点大小
                val minCount = -yMinusCount

                it.drawText(
                    "${(minCount + i).toFloat() / 10}", 0f,
                    startPointY.y - i * currentGridWH, xyPaint
                )
            }

            //竖向网格线
            for (i in 1..10 step 1) {
                it.drawLine(
                    circlePoint.x + currentGridWH * i, startPointY.y,
                    circlePoint.x + currentGridWH * i, endPointY.y, gridPaint
                )

                it.drawText(
                    "${i.toFloat() / 10}", circlePoint.x + currentGridWH * i,
                    (circlePoint.y + 0.5f * fontPadding), xyPaint
                )
            }
            //绘制曲线
            it.drawPath(orbitPath, orbitPaint)
        }
    }
}