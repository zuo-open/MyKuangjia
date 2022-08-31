package com.zxl.componentgallery.components.fingergesture.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.utils.MultiLanguageUtils
import com.zxl.componentgallery.components.fingergesture.widgets.GestureLockLayout.TextHolder.Companion.LESS_THAN_MINCOUNT
import com.zxl.componentgallery.R
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import kotlin.math.pow
import kotlin.math.sqrt


//手指登陆圆环控件
class GestureLockLayout @JvmOverloads constructor(
    context: Context?,
    val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //圆形控件是否有圆环
    private var childHasRoundBorder: Boolean = false

    //布局每行排列几个圆环控件
    private var layoutChildCount = Int.MAX_VALUE

    //是否允许时间交互
    private var allowInteract: Boolean = false

    //当前画笔
    private lateinit var currentPaint: Paint

    //正确画笔
    private val rightPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    //错误画笔
    private val errorPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    //控件宽高
    private var mWidth: Int = -1
    private var mHeight = -1

    //密码容器
    private var passwordContainer = ArrayList<Int>()

    //圆形控件集合
    private lateinit var gestureLockCircleLists: Array<GestureLockCircleView?>

    //划给每个节点的宽度
    private var countWidth = -1

    //滑动路径
    private val lockPath = Path()

    //引导线（还没有画完或者没有连接到相应的势登陆圆形控件）
    private var startLineX = 0
    private var endLineX = 0
    private var startLineY = 0
    private var endLineY = 0

    //按下时的x,y
    private var downX = 0
    private var downY = 0

    //移动时的x,y
    private var moveX = 0
    private var moveY = 0

    //是否点击在圆形控件的范围内
    private var clickOnCircleView = false

    //view判定比例
    private val viewScale = 0.2

    //da当前密码判定是否失败过
    private var ifCheckInErr = false

    //按下view在圆形控件集合的index
    private var childStartIndex = -1

    //最后移动view在圆形控件集合中的index
    private var childEndIndex = -1

    //circle的半径(含外边圆环)
    private var cicleViewRadius = -1

    //手势登陆处理回调
    private var listener: OnGestureListener? = null

    //当前模式
    private var currentMode = -1

    //保存的密码
    private var needCheckPassword = ArrayList<Int>()

    //重置尝试次数
    private var resetCount = 0

    /*    //最大尝试次数
        private val maxResetCount = 4*/
    //临时存储密码容器
    private var tempPasswordContainer: ArrayList<Int>? = null

    //效验密码次数
    private var checkPasswordCount = 0

    //效验密码最大次数
    private var maxCheckPasswordCount = 5

    //正常文本颜色
    private var commonTextColor: Int = -1

    //错误文本颜色
    private var errorTextColor: Int = -1

    companion object {
        const val MODE_RESTE = 1//重置模式
        const val MODE_CHECK_PASSWORD = 2//检查密码模式
    }

    //是否显示绘制痕迹
    var showDraw = true

    //颜色 : 设置前内圆颜色、设置中内圆颜色、错误时内圆颜色；设置前外圆颜色、设置时外圆颜色、错误时外圆颜色
    var ringInnerNormalColor: Int = 0
    var ringOuterNormalColor: Int = 0
    var ringInnerDrawColor: Int = 0
    var ringOuterDrawColor: Int = 0
    var ringInnerErrColor: Int = 0
    var ringOuterErrColor: Int = 0

    //内圆样式，外圆样式
    var ringInnerStyle: Int = 0
    var ringOuterStyle: Int = 0

    //内圆边框宽，外圆边框宽
    var ringInnerStrokeWidth: Float = 0F
    var ringOuterStrokeWidth: Float = 0F


    init {
        val typedArray =
            context?.obtainStyledAttributes(attrs, R.styleable.GestureLockLayout)
        try {
            if (typedArray != null) {
                childHasRoundBorder =
                    typedArray.getBoolean(R.styleable.GestureLockLayout_layoutChildCount, false)
                layoutChildCount =
                    typedArray.getInt(R.styleable.GestureLockLayout_layoutChildCount, 3)
                allowInteract =
                    typedArray.getBoolean(R.styleable.GestureLockLayout_allowInteract, false)
                commonTextColor =
                    typedArray.getColor(R.styleable.GestureLockLayout_commonTextColor, Color.GRAY)
                errorTextColor =
                    typedArray.getColor(R.styleable.GestureLockLayout_errorTextColor, Color.RED)


                //初始化各项属性
                showDraw = typedArray.getBoolean(R.styleable.GestureLockLayout_showDraw, true)

                ringInnerNormalColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringInnerNormalColor,
                    ContextCompat.getColor(context, R.color.gesture_circle_fill_default)
                )
                ringOuterNormalColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringOuterNormalColor, Color.TRANSPARENT
                )
                ringInnerDrawColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringInnerDrawColor,
                    ContextCompat.getColor(context, R.color.gesture_circle_fill)
                )
                ringOuterDrawColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringOuterDrawColor,
                    ContextCompat.getColor(context, R.color.gesture_circle_border)
                )
                ringInnerErrColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringInnerErrColor,
                    ContextCompat.getColor(context, R.color.colorCheckedErr)
                )
                ringOuterErrColor = typedArray.getColor(
                    R.styleable.GestureLockLayout_ringOuterErrColor,
                    ContextCompat.getColor(context, R.color.colorRoundBorderErr)
                )

                ringInnerStyle = typedArray.getInt(R.styleable.GestureLockLayout_ringInnerStyle, 0)
                ringOuterStyle = typedArray.getInt(R.styleable.GestureLockLayout_ringOuterStyle, 0)
                ringInnerStrokeWidth =
                    typedArray.getDimension(R.styleable.GestureLockLayout_ringInnerStrokeWidth, 4f)
                ringOuterStrokeWidth =
                    typedArray.getDimension(R.styleable.GestureLockLayout_ringOuterStrokeWidth, 4f)

            }
            //初始化画笔
            init()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray?.recycle()
        }
    }

    //初始化画笔
    private fun init() {
        //为了顺利接收事件，需要开启click;因为你如果不设置，，就只能收到down，其他的一概收不到
        isClickable = true
        //设置透明背景 ；这里如果不设置，onDraw将不会执行；原因:这是一个ViewGroup，本身是容器，
        // 不具备自我绘制功能，但是这里设置了背景色，就说明有东西需要绘制，onDraw就会执行
        setBackgroundColor(Color.TRANSPARENT)
        //正确画笔
        rightPaint.style = Paint.Style.STROKE
        rightPaint.color = ContextCompat.getColor(context, R.color.gesture_layout_right)
        //错误画笔
        errorPaint.style = Paint.Style.STROKE
        errorPaint.color = ContextCompat.getColor(context, R.color.gesture_layout_error)

        currentPaint = rightPaint


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取测量后宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        //为了确保是一个正方形且能正确显示出手势解锁布局，取宽高的最小值
        if (mWidth > mHeight) mWidth = mHeight else mHeight = mWidth
        if (!this::gestureLockCircleLists.isInitialized) {
            //创建节点容器
            gestureLockCircleLists =
                arrayOfNulls(layoutChildCount * layoutChildCount)
            countWidth = mWidth / layoutChildCount
            for (i in gestureLockCircleLists.indices) {
                //创建手上登录圆点控件并存入容器
                gestureLockCircleLists[i] = getCircleView(mHeight)
                //设置id
                gestureLockCircleLists[i]?.id = i + gestureLockCircleLists[i]?.hashCode()!!
                //摆放view
                val layoutParams = LayoutParams(countWidth, countWidth)

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % layoutChildCount != 0) {
                    layoutParams.addRule(
                        RIGHT_OF,
                        gestureLockCircleLists[i - 1]?.id ?: 0
                    )
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > layoutChildCount - 1) {
                    layoutParams.addRule(
                        BELOW,
                        gestureLockCircleLists[i - layoutChildCount]?.id ?: 0
                    )
                }
                layoutParams.setMargins(0, 0, 0, 0)
                addView(gestureLockCircleLists[i], layoutParams)
            }
        }

    }

    /*  //绘制子view 不想让子view覆盖画线请使用此方法
      override fun dispatchDraw(canvas: Canvas?) {

          super.dispatchDraw(canvas)
          *//*  //绘制完子view再画线
          if (this::gestureLockCircleLists.isInitialized && allowInteract) {
              //绘制滑动轨迹
              drawLockPath(canvas)
              //绘制引导线
              drawLinePath(canvas)
          }*//*
    }*/

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制完子view再画线
        if (this::gestureLockCircleLists.isInitialized && allowInteract && showDraw) {
            //绘制滑动轨迹
            drawLockPath(canvas)
            //绘制引导线
            drawLinePath(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //允许绘制图案在绘制图案
        if (allowInteract) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    //清除先前绘制的轨迹
                    lockPath.reset()
                    //抬起时将所有circleView的状态重置为初始化
                    resetAllCircleView(GestureLockCircleView.STATUS_DEFAULT)
                    currentPaint = rightPaint
                    downX = event.x.toInt()
                    downY = event.y.toInt()
                    clickOnCircleView = false
                    //是否点击在circleView上
                    val circleView = isClickOnCircleView(downX, downY)
                    if (circleView != null) {
                        //找到了点击的控件
                        clickOnCircleView = true
                        if (!ifCheckInErr) {
                            circleView.setMode(GestureLockCircleView.STATUS_DRAW)
                        } else {
                            circleView.setMode(GestureLockCircleView.STATUS_ERROR)
                        }
                        childStartIndex = getClickedChildIndex(downX, downY)
                        //重置手势密码
                        lockPath.reset()
                        restPassword()
                        addPassword(childStartIndex)
                        //添加路径
                        //获取点击view的中心位置
                        val point = getViewCenter(gestureLockCircleLists[childStartIndex])
                        if (point != null) {
                            //移动到该view的中心点
                            lockPath.moveTo(point.x.toFloat(), point.y.toFloat())
                            //引导线的起始坐标
                            startLineX = point.x
                            startLineY = point.y
                        }

                    }

                }
                MotionEvent.ACTION_MOVE -> {
                    var checkNeedChangeCircleView: GestureLockCircleView? = null
                    //找到点击的view再执行相关代码
                    if (clickOnCircleView) {
                        moveX = event.x.toInt()
                        moveY = event.y.toInt()
                        childEndIndex = getClickedChildIndex(moveX, moveY)
                        //-1标识没有找到对应的区域
                        val flag1 = childStartIndex != -1 && childEndIndex != -1//没有获取到正确的对应区域
                        val flag2 = childStartIndex != childEndIndex//在同一个区域内不需要画线
                        val flag3 =
                            checkRepetition(childEndIndex)//不允许密码值重复,这里要检查当前这个区域是不是已经在lockPathArr里面
                        //满足所有条件则绘制轨迹线
                        if (flag1 && flag2 && !flag3) {
                            val point = getViewCenter(gestureLockCircleLists[childEndIndex])
                            val circleView = isClickOnCircleView(moveX, moveY)
                            if (point != null && circleView != null) {
                                //判断当前路径上是否还有未选中的点
                                val checkNeedChangeCircleViewIndex =
                                    checkNeedChangeCircleView(point)
                                if (checkNeedChangeCircleViewIndex != -1) {
                                    checkNeedChangeCircleView =
                                        gestureLockCircleLists[checkNeedChangeCircleViewIndex]
                                    addPassword(checkNeedChangeCircleViewIndex)
                                }
                                addPassword(childEndIndex)
                                childStartIndex = childEndIndex
                                //判断当前路径上是否还有未选中的点
                                if (!ifCheckInErr) {
                                    circleView.setMode(GestureLockCircleView.STATUS_DRAW)
                                    checkNeedChangeCircleView?.setMode(GestureLockCircleView.STATUS_DRAW)
                                } else {
                                    circleView.setMode(GestureLockCircleView.STATUS_ERROR)
                                    checkNeedChangeCircleView?.setMode(GestureLockCircleView.STATUS_ERROR)
                                }
                                //判断路径(就是以circleView边缘进行画线)
                                //  val updatePoint = updatePath(point)
                                //添加路径
                                lockPath.lineTo(point.x.toFloat(), point.y.toFloat())
                                startLineX = point.x
                                startLineY = point.y
                            }
                        }
                    }
                    endLineX = moveX
                    endLineY = moveY
                    postInvalidate()//刷新视图
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (clickOnCircleView) {
                        //重置引导线
                        resetLinePath()
                        //清除路径信息
                        //lockPath.reset()
                        //postInvalidate()
                        //抬起时将所有circleView的状态重置为初始化
                        resetAllCircleView(GestureLockCircleView.STATUS_DEFAULT)
                        //获取当前绘制的密码信息
                        getCurrentPassword()
                        if (passwordContainer.size >= 4) {
                            when (currentMode) {
                                MODE_RESTE -> //如果处于reset模式下，执行rest的回调
                                    onReset()
                                MODE_CHECK_PASSWORD -> //检查模式下，执行onCheck
                                    onCheck()
                                else -> throw RuntimeException("未指定GestLockLayout模式")
                            }
                        } else {
                            sendMessage(LESS_THAN_MINCOUNT, errorTextColor, true)
                            currentPaint = errorPaint
                            ifCheckInErr = true
                            //将选中的circleView变为红色
                            resetClickCircleView()
                            postInvalidate()
                            resetAllStatus(1500L)
                        }

                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    //判断当前路径上是否还有未选中的点
    private fun checkNeedChangeCircleView(point: Point): Int {
        val x = point.x
        val y = point.y
        //x,yf方向上的差值
        val deltaX = x - startLineX
        val deltaY = y - startLineY
        if (deltaX > 0 && deltaY > 0) {
            //右下
            if (childStartIndex == 0 && childEndIndex == 8) {
                return 4
            }
        }
        if (deltaX > 0 && deltaY == 0) {
            //右
            if (childStartIndex == 0 && childEndIndex == 2) {
                return 1
            }
            if (childStartIndex == 3 && childEndIndex == 5) {
                return 4
            }
            if (childStartIndex == 6 && childEndIndex == 8) {
                return 7
            }
        }

        if (deltaX > 0 && deltaY < 0) {
            //右上
            if (childStartIndex == 6 && childEndIndex == 2) {
                return 4
            }
        }

        if (deltaX < 0 && deltaY > 0) {
            //左下
            if (childStartIndex == 2 && childEndIndex == 6) {
                return 4
            }
        }

        if (deltaX < 0 && deltaY == 0) {
            //左
            if (childStartIndex == 2 && childEndIndex == 0) {
                return 1
            }
            if (childStartIndex == 5 && childEndIndex == 3) {
                return 4
            }
            if (childStartIndex == 8 && childEndIndex == 6) {
                return 7
            }
        }

        if (deltaX < 0 && deltaY < 0) {
            //左上
            if (childStartIndex == 8 && childEndIndex == 0) {
                return 4
            }
        }
        //下
        if (deltaX == 0 && deltaY > 0) {
            if (childStartIndex == 0 && childEndIndex == 6) {
                return 3
            }
            if (childStartIndex == 1 && childEndIndex == 7) {
                return 4
            }
            if (childStartIndex == 2 && childEndIndex == 8) {
                return 5
            }
        }

        //上
        if (deltaX == 0 && deltaY < 0) {
            if (childStartIndex == 6 && childEndIndex == 0) {
                return 3
            }
            if (childStartIndex == 7 && childEndIndex == 1) {
                return 4
            }
            if (childStartIndex == 8 && childEndIndex == 2) {
                return 5
            }
        }
        return -1
    }

    //获取当前绘制密码
    private fun getCurrentPassword() {
        if (listener != null) {
            listener?.onPasswordFinish(passwordContainer)
        }

    }

    //重置所有circleView状态
    private fun resetAllCircleView(mode: Int) {
        if (this::gestureLockCircleLists.isInitialized) {
            for (i in gestureLockCircleLists.indices) {
                gestureLockCircleLists[i]?.setMode(mode)
            }
        }

    }


    private fun resetLinePath() {
        startLineX = 0
        startLineY = 0
        endLineX = 0
        endLineY = 0
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //绘制引导线
    private fun drawLinePath(canvas: Canvas?) {
        //只有确定了起始点才能开始绘制
        if (startLineX != 0 && startLineY != 0) {
            canvas?.drawLine(
                startLineX.toFloat(), startLineY.toFloat(),
                endLineX.toFloat(), endLineY.toFloat(), currentPaint
            )
        }
    }

    //绘制滑动轨迹
    private fun drawLockPath(canvas: Canvas?) {
        canvas?.drawPath(lockPath, currentPaint)
    }

    //获取手势解锁圆形控件
    private fun getCircleView(mHeight: Int): GestureLockCircleView {

        //计算实心圆半径
        val ration = layoutChildCount.toDouble().pow(3.0) + 0.5f
        //设置实心圆
        //设置圆环
        val ringRation = layoutChildCount.toDouble().pow(2.0) + 0.5f

        val gestureLockCircleView = GestureLockCircleView(context)

        //初始化各项属性
        gestureLockCircleView.showDraw = showDraw

        gestureLockCircleView.ringInnerNormalColor = ringInnerNormalColor
        gestureLockCircleView.ringOuterNormalColor = ringOuterNormalColor
        gestureLockCircleView.ringInnerDrawColor = ringInnerDrawColor
        gestureLockCircleView.ringOuterDrawColor = ringOuterDrawColor
        gestureLockCircleView.ringInnerErrColor = ringInnerErrColor
        gestureLockCircleView.ringOuterErrColor = ringOuterErrColor

        gestureLockCircleView.ringInnerStyle = ringInnerStyle
        gestureLockCircleView.ringOuterStyle = ringOuterStyle

        gestureLockCircleView.ringInnerStrokeWidth = ringInnerStrokeWidth
        gestureLockCircleView.ringOuterStrokeWidth = ringOuterStrokeWidth
        gestureLockCircleView.ringInnerRadius = (mHeight / ration).toFloat()
        gestureLockCircleView.ringOuterRadius = (mHeight / ringRation).toFloat()

        cicleViewRadius = (mHeight / ringRation).toInt()

        //根据宽度设置画笔
        rightPaint.strokeWidth = (mHeight / ration).toFloat() * 0.1f
        errorPaint.strokeWidth = (mHeight / ration).toFloat() * 0.1f
        //设置为默认模式
        gestureLockCircleView.setMode(GestureLockCircleView.STATUS_DEFAULT)
        return gestureLockCircleView
    }

    //是否点击在GestureLockCircleView的范围内
    private fun isClickOnCircleView(downX: Int, downY: Int): GestureLockCircleView? {
        for (index in gestureLockCircleLists.indices) {
            val view = gestureLockCircleLists[index]
            if (isClickOnView(downX, downY, view)) {
                return view
            }
        }
        return null
    }

    //获取点击view的索引
    private fun getClickedChildIndex(x: Int, y: Int): Int {
        for (i in gestureLockCircleLists.indices) {
            val view = gestureLockCircleLists[i]
            if (isClickOnView(x, y, view)) {//
                return i
            }
        }
        return -1
    }

    //是否点击在当前view上
    private fun isClickOnView(downX: Int, downY: Int, view: GestureLockCircleView?): Boolean {
        val rect = Rect()
        if (view == null) {
            return false
        } else {
            //获取view占据的rect
            view.getHitRect(rect)
            //获取view宽高
            val width = view.width
            val height = view.height
            //缩小判定点击的范围点击
            val realLeft = (rect.left + viewScale * width).toInt()
            val realTop = (rect.top + viewScale * height).toInt()
            val realRight = (rect.right - viewScale * width).toInt()
            val realBottom = (rect.bottom - viewScale * height).toInt()
            //新的判定区域
            val rect1 = Rect(realLeft, realTop, realRight, realBottom)

            if (rect1.contains(downX, downY)) {
                return true
            }
            return false
        }
    }


    //重置密码
    private fun restPassword() {
        passwordContainer.clear()
    }


    //添加密码
    private fun addPassword(password: Int) {
        passwordContainer.add(password)
    }

    //获取view的中心点
    private fun getViewCenter(circleView: GestureLockCircleView?): Point? {
        if (circleView != null) {
            val rect = Rect()
            circleView.getHitRect(rect)
            return Point(rect.left + rect.width() / 2, rect.top + rect.height() / 2)
        }
        return null

    }

    //检查是否有重复的密码
    private fun checkRepetition(childEndIndex: Int): Boolean {
        return passwordContainer.contains(childEndIndex)
    }

    //判断路径
    private fun updatePath(point: Point): Point {
        var x = point.x
        var y = point.y
        val radius = cicleViewRadius / sqrt(2.0)

        if (startLineX != x && startLineY != y) {
            if (startLineX - x < 0 && startLineY - y < 0) {
                //下一个节点在上一个节点的右下部
                lockPath.moveTo(
                    (startLineX + radius).toFloat(),
                    (startLineY + radius).toFloat()
                )
                x -= radius.toInt()
                y -= radius.toInt()
            } else if (startLineX - x < 0 && startLineY - y > 0) {
                //下一个节点在上一个节点的右上部
                lockPath.moveTo((startLineX + radius).toFloat(), (startLineY - radius).toFloat())
                x -= radius.toInt()
                y += radius.toInt()
            } else if (startLineX - x > 0 && startLineY - y < 0) {
                //下一个节点在上一个节点的左下部
                lockPath.moveTo((startLineX - radius).toFloat(), (startLineY + radius).toFloat())
                x += radius.toInt()
                y -= radius.toInt()
            } else if (startLineX - x > 0 && startLineY - y > 0) {
                //下一个节点在上一个节点的左上部
                lockPath.moveTo((startLineX - radius).toFloat(), (startLineY - radius).toFloat())
                x += radius.toInt()
                y += radius.toInt()
            }


        } else {
            //下一个节点在上一个节点的左边
            if (startLineX - x > 0) {
                lockPath.moveTo((startLineX - cicleViewRadius).toFloat(), startLineY.toFloat())
                x += cicleViewRadius
            } else if (startLineX - x < 0) {
                //下一个节点在上一个节点的右边
                lockPath.moveTo((startLineX + cicleViewRadius).toFloat(), startLineY.toFloat())
                x -= cicleViewRadius
            }
            //下一个节点在上一个节点的上部
            if (startLineY - y > 0) {
                lockPath.moveTo(startLineX.toFloat(), (startLineY - cicleViewRadius).toFloat())
                y += cicleViewRadius
            } else if (startLineY - y < 0) {
                //下一个节点在上一个节点的下部
                lockPath.moveTo(startLineX.toFloat(), (startLineY + cicleViewRadius).toFloat())
                y -= cicleViewRadius
            }
        }
        return Point(x, y)
    }


    //检查密码
    private fun onCheck() {
        checkPasswordCount++
        if (checkPasswordCount < maxCheckPasswordCount) {
            val comparePassword = comparePassword(needCheckPassword, passwordContainer)
            if (comparePassword) {
                //验证成功
                listener?.checkGesturePasswordSuccess()
                currentPaint = rightPaint
                ifCheckInErr = false
                //恢复初始化状态
                resetAllStatus(0L)
            } else {
                //验证失败
                currentPaint = errorPaint
                ifCheckInErr = true
                //将选中的circleView变为红色
                resetClickCircleView()
                postInvalidate()
                sendMessage(
                    String.format(
                        TextHolder.CHECK_PASSWORDERROR,
                        maxCheckPasswordCount - checkPasswordCount
                    ), errorTextColor, true
                )
                //恢复默认状态
                resetAllStatus(1500L)
            }
        } else {
            //超过最大绘制次数
            currentPaint = errorPaint
            ifCheckInErr = true
            //将选中的circleView变为红色
            resetClickCircleView()
            invalidate()
            sendMessage(String.format(TextHolder.CHECK_PASSWORDERROR, 0), errorTextColor, true)
            resetAllStatus(1500L)
            //回调方法
            listener?.overMaxCheckCount()
        }

    }

    //将选中的circleView变为红色
    private fun resetClickCircleView() {
        for (i in gestureLockCircleLists.indices) {
            if (passwordContainer.contains(i)) {
                gestureLockCircleLists[i]?.setMode(GestureLockCircleView.STATUS_ERROR)
            }
        }
    }

    //恢复默认状态（circleView点击后的颜色，GestureLockLayouy的画笔颜色）
    private fun resetAllStatus(time: Long) {
        /* allowInteract=false*/
        handler.postDelayed({
            //清除路径信息
            lockPath.reset()
            //circleView恢复默认状态
            resetAllCircleView(GestureLockCircleView.STATUS_DEFAULT)
            //设置默认画笔
            currentPaint = rightPaint
            ifCheckInErr = false
            allowInteract = true
            //引起重绘
            postInvalidate()
        }, time)

    }

    //reset
    private fun onReset() {
        //设置设置次数
        resetCount += 1
        /* if (resetCount < 4) {*/
        if (resetCount == 1) {
            //保存密码
            tempPasswordContainer = ArrayList()
            tempPasswordContainer?.addAll(passwordContainer)
            //提示再次重绘
            sendMessage(TextHolder.RESET_TRY_AGAIN, commonTextColor, false)
            //重置绘制路径
            lockPath.reset()
            postInvalidate()
        } else {
            //比较两次输入的手势密码是否相同
            val flag = comparePassword(tempPasswordContainer, passwordContainer)
            if (flag) {
                //重置绘制路径
                lockPath.reset()
                postInvalidate()
                //设置密码成功
                listener?.resetPasswordSuccess(copyPassword(tempPasswordContainer))
            } else {
                currentPaint = errorPaint
                ifCheckInErr = true
                //将选中的circleView变为红色
                resetClickCircleView()
                postInvalidate()
                //提示绘制不一样
                sendMessage(TextHolder.RESET_NOT_EQUAL, errorTextColor, true)
                //恢复默认状态
                resetAllStatus(1500L)
            }
        }

    } /*else {
            //超过最大尝试次数
            if (listener != null) {
                listener?.overResetCount(maxResetCount)
            }
        }

    }*/

    //拷贝密码
    private fun copyPassword(tempPasswordContainer: ArrayList<Int>?): List<Int> {
        val list = ArrayList<Int>()
        if (tempPasswordContainer != null) {

            for (i in 0 until tempPasswordContainer.size) {
                list.add(tempPasswordContainer[i])
            }

        }
        return list
    }

    //比较密码是否相同
    private fun comparePassword(
        tempPasswordContainer: ArrayList<Int>?,
        passwordContainer: ArrayList<Int>
    ): Boolean {
        if (tempPasswordContainer != null && tempPasswordContainer.size == passwordContainer.size) {
            for (i in tempPasswordContainer.indices) {
                if (tempPasswordContainer[i] != passwordContainer[i]) {
                    return false
                }
            }
            return true
        }
        return false
    }

    //设置回调
    fun setOnGestureListener(listener: OnGestureListener) {
        this.listener = listener
    }

    //通用消息
    fun sendMessage(content: String, color: Int, showAnimator: Boolean) {
        if (listener != null) {
            listener?.sendMessage(content, color, showAnimator)
        }
    }

    //设置重置模式
    fun setResetMode() {
        currentMode = MODE_RESTE
    }

    //设置密码查看模式
    fun setCheckPasswordMode(needCheckPassword: String) {
        currentMode = MODE_CHECK_PASSWORD
        needCheckPassword.toList().forEach {
            val i = it.toString().toInt()
            this.needCheckPassword.add(i)
        }
    }


    /**
     * 提供一个方法，绘制密码点，但是只绘制 圆圈，不绘制引导线和轨迹线
     */
    fun refreshPwdKeyboard(pwd: List<Int?>?) {
        try {
            for (i in 0 until layoutChildCount * layoutChildCount) { //先把所有的点都设置为notChecked
                gestureLockCircleLists[i]?.setMode(GestureLockCircleView.STATUS_DEFAULT)
            }
            if (null != pwd) for (i in pwd.indices) { //再把密码中的点，设置为checked
                gestureLockCircleLists.getOrNull(pwd.getOrNull(i) ?: -1)?.setMode(
                    GestureLockCircleView.STATUS_DRAW
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            //这里有可能发生数组越界，因为 本类的各个对象时相互独立的，方阵行数可能不同
            e.printStackTrace()
        }
    }


    interface OnGestureListener {
        //获取设置的密码
        fun onPasswordFinish(password: List<Int>)

        //超过最大尝试次数
        fun overResetCount(maxRestCount: Int)

        //通用消息通知
        fun sendMessage(content: String, color: Int, showAnimator: Boolean)

        //重置手势密码成功
        fun resetPasswordSuccess(list: List<Int>)

        //手势密码验证成功
        fun checkGesturePasswordSuccess()


        //超过效验密码最大次数
        fun overMaxCheckCount()
    }


    class TextHolder {
        companion object {
            val INPUT_ORIGINAL_PASSWORD =
                BaseContext.getApplication().getString(R.string.text_input_original_password)
            val REST_PASSWORD = BaseContext.getApplication().getString(R.string.text_draw_gesture)
            val RESET_PASSWORD =
                BaseContext.getApplication().getString(R.string.text_reset_password)
            val RESET_TRY_AGAIN =
                BaseContext.getApplication().getString(R.string.text_reset_try_again)
            val RESET_NOT_EQUAL =
                BaseContext.getApplication().getString(R.string.text_reset_not_requal)
            val CHECK_PASSWORDERROR =
                BaseContext.getApplication().getString(R.string.text_check_password)
            val LESS_THAN_MINCOUNT =
                BaseContext.getApplication().getString(R.string.text_less_than_mincount)
        }
    }


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

        var ringInnerStyle: Int = 0
        var ringOuterStyle: Int = 0
        var ringInnerStrokeWidth: Float = 3F
        var ringOuterStrokeWidth: Float = 3F

        //内圆画笔
        private val ringInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        //外圆画笔
        private val ringOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        //内圆的半径
        var ringInnerRadius: Float = 0F

        //外圆半径
        var ringOuterRadius: Float = 0F

        override fun onDraw(canvas: Canvas?) {
            canvas?.save()
            //获取中心点
            val centerX = width / 2
            val centerY = height / 2
            //有外环
            canvas?.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                ringOuterRadius,
                ringOuterPaint
            )
            //画圆心
            canvas?.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                ringInnerRadius,
                ringInnerPaint
            )
            canvas?.restore()
        }

        private fun updatePaint() {
            ringInnerPaint.style = if (ringInnerStyle == 2) Paint.Style.STROKE else Paint.Style.FILL
            ringOuterPaint.style = if (ringOuterStyle == 2) Paint.Style.STROKE else Paint.Style.FILL
            ringInnerPaint.strokeWidth = ringInnerStrokeWidth
            ringOuterPaint.strokeWidth = ringOuterStrokeWidth
        }


        companion object {
            const val STATUS_DEFAULT = 0  //初始化
            const val STATUS_ERROR = 1    //密码错误
            const val STATUS_DRAW = 2    //绘制
        }

        fun setMode(mode: Int) {
            updatePaint()
            when (mode) {
                //默认状态
                STATUS_DEFAULT -> {
                    ringInnerPaint.color = ringInnerNormalColor
                    ringOuterPaint.color = ringOuterNormalColor
                }
                //绘制错误状态
                STATUS_ERROR -> {
                    if (showDraw) {
                        ringInnerPaint.color = ringInnerErrColor
                        ringOuterPaint.color = ringOuterErrColor
                    }
                }
                //绘制状态
                STATUS_DRAW -> {
                    if (showDraw) {
                        ringInnerPaint.color = ringInnerDrawColor
                        ringOuterPaint.color = ringOuterDrawColor
                    }
                }
            }
            postInvalidate()
        }
    }

}