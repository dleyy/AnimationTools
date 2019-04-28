package com.example.annimationshow.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.annimationshow.util.dp2px


class OrbitView(context: Context, att: AttributeSet?) : View(context, att) {

    //视图内边距
    private val viewPadding = dp2px(context, 20)

    //留给字体的偏移
    private val fontPadding = dp2px(context, 15)

    // 网格默认 宽高
    private val defaultGridWH = dp2px(context, 15)

    //默认坐标比例
    private val defaultRate = 10.0

    //字体大小
    private val fontSize = 16f

    //网线大小
    private val lineSize = 14f

    //默认最高点
    private val defaultMaxPoint = PointF(0f, 1f)

    //默认最低点
    private val defaultMinPoint = PointF(0f, 0f)

    //默认 y 轴的网格数目
    private val defaultYCellNum = 10

    //当前 y 轴的网格数目
    private var currentYCellNum = defaultYCellNum

    //当前 网格 宽高
    private var currentGridWH = defaultGridWH

    // 视图宽高
    private var mViewWidth = 0f
    private var mViewHeight = 0f

    //圆点坐标。
    private var circlePoint: PointF = PointF()

    //存储最“大”点坐标
    private var endPoint: PointF = PointF()

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
    private lateinit var points: List<PointF>

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
    open fun setPointFs(list: List<PointF>) {
        this.points = list

        //计算最高点与最低点
        for (point: PointF in list) {
            if (point.y > maxPoint.y) {
                maxPoint = point
            } else if (point.y < minPoint.y) {
                minPoint = point
            }
        }

        // 计算 网格 尺寸
//        currentGridWH = Math.min(
//            (mViewWidth - fontPadding) / currentYCellNum,
//            (mViewHeight - fontPadding) / (maxPoint.y - minPoint.y)
//        )

        calculateStartAndEndPoint()

        buildPath()

        invalidate()
    }

    /**
     * 构建路径
     */
    private fun buildPath() {
        orbitPaint.reset()
        if (points.isNotEmpty()) {
            var width = currentYCellNum * currentGridWH
            for (i in 0 until points.size step 1) {
                var currentPointF = points[i]
                orbitPath.moveTo(circlePoint.x,circlePoint.y)
                orbitPath.lineTo(
                    currentPointF.x * width,
                    (circlePoint.y
                            - currentPointF.y * defaultRate * currentGridWH).toFloat()
                )
            }
        }

    }

    /**
     * 计算 圆心 以及 结束点坐标。
     */
    private fun calculateStartAndEndPoint() {

        // 计算 y轴上半轴 以及 y轴下半轴的 网格 数目
        val yPlusCount = Math.abs(Math.ceil(maxPoint.y.toDouble())) * defaultRate
        val yMinusCount = Math.abs(Math.floor(minPoint.y.toDouble())) * defaultRate

        currentYCellNum = (yPlusCount + yMinusCount).toInt()

        //计算圆点坐标
        circlePoint.x = fontPadding
        circlePoint.y = (mViewHeight - yMinusCount * currentGridWH).toFloat()

        // 计算x轴最远距离 以及 y轴最远距离
        endPoint.x = mViewWidth - fontPadding
        endPoint.y = mViewHeight - fontPadding

    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        xyPaint.let {
            it.isAntiAlias = true
            it.color = resources.getColor(android.R.color.black)
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
            it.color = resources.getColor(android.R.color.holo_red_light)
            it.style = Paint.Style.FILL_AND_STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //y轴
            it.drawLine(
                circlePoint.x, circlePoint.y,
                circlePoint.x, 0f, xyPaint
            )
            //x轴
            it.drawLine(
                circlePoint.x, circlePoint.y,
                endPoint.x, circlePoint.y, xyPaint
            )

            //横向网格线
            for (i in minPoint.y.toInt() * currentYCellNum..
                    maxPoint.y.toInt() * currentYCellNum step 1) {
                it.drawLine(
                    circlePoint.x, circlePoint.y - i * currentGridWH,
                    endPoint.x, circlePoint.y - i * currentGridWH, gridPaint
                )
                if (i != 0) {
                    it.drawText(
                        "${i.toFloat() / currentYCellNum}", 0f,
                        circlePoint.y - i * currentGridWH, xyPaint
                    )
                } else {
                    it.drawText(
                        "0", 0f, circlePoint.y,
                        xyPaint
                    )
                }
            }

            //竖向网格线
            for (i in 1..currentYCellNum step 1) {
                it.drawLine(
                    circlePoint.x + currentGridWH * i, circlePoint.y,
                    circlePoint.x + currentGridWH * i, 0f, gridPaint
                )
                it.drawText(
                    "${i.toFloat() / currentYCellNum}", circlePoint.x + currentGridWH * i,
                    circlePoint.y + fontPadding, xyPaint
                )
            }

            //绘制曲线
            it.drawPath(orbitPath, orbitPaint)
        }
    }
}