package com.pansoft.me.widget.gesture

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

//手势登陆圆形控件
class GestureLockCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //是否显示绘制痕迹
    var showDraw = true

    //颜色 : 设置前内圆颜色、设置中内圆颜色、错误时内圆颜色；设置前外圆颜色、设置时外圆颜色、错误时外圆颜色
    var ringInnerNormalColor: Int = 0
    var ringOuterNormalColor: Int = 0
    var ringInnerDrawColor: Int = 0
    var ringOuterDrawColor: Int = 0
    var ringInnerErrColor: Int = 0
    var ringOuterErrColor: Int = 0

    //内圆画笔
    private val ringInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //外圆画笔
    private val ringOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun setRingInnerStyle(ringInnerStyle: Int){
        ringInnerPaint.style = if (ringInnerStyle == 2) Paint.Style.STROKE else Paint.Style.FILL
    }

    fun setRingOuterStyle(ringOuterStyle: Int){
        ringOuterPaint.style = if (ringOuterStyle == 2) Paint.Style.STROKE else Paint.Style.FILL
    }

    fun setRingInnerStrokeWidth(ringInnerStrokeWidth: Float){
        ringInnerPaint.strokeWidth = ringInnerStrokeWidth
    }

    fun setRingOuterStrokeWidth(ringOuterStrokeWidth: Float){
        ringOuterPaint.strokeWidth = ringOuterStrokeWidth
    }

    //内圆的半径
    var ringInnerRadius: Float=0F
    //外圆半径
    var ringOuterRadius: Float=0F

    override fun onDraw(canvas: Canvas?) {
        canvas?.save()
        //获取中心点
        val centerX = width / 2
        val centerY = height / 2
        //有外环
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), ringOuterRadius, ringOuterPaint)
        //画圆心
        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), ringInnerRadius, ringInnerPaint)
        canvas?.restore()
    }


    companion object {
        const val STATUS_DEFAULT = 0  //初始化
        const val STATUS_ERROR = 1    //密码错误
        const val STATUS_DRAW = 2    //绘制
    }

    fun setMode(mode: Int) {
        when (mode) {
            //默认状态
            STATUS_DEFAULT -> {
                ringInnerPaint.color = ringInnerNormalColor
                ringOuterPaint.color = ringOuterNormalColor
            }
            //绘制错误状态
            STATUS_ERROR -> {
                if(showDraw){
                    ringInnerPaint.color = ringInnerErrColor
                    ringOuterPaint.color = ringOuterErrColor
                }
            }
            //绘制状态
            STATUS_DRAW -> {
                if(showDraw){
                    ringInnerPaint.color = ringInnerDrawColor
                    ringOuterPaint.color = ringOuterDrawColor
                }
            }
        }
        postInvalidate()
    }
}