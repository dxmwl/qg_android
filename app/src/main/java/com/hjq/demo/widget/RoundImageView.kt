package com.hjq.demo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.hjq.demo.R


/**
 * author : WilliamYang
 * date : 2022/9/18 14:54
 * description : 可设置 圆角、外边框 的 ImageView
 *
 * 使用方式：
 *
 *    1. 使用 riv_radius 设置4个角均为圆角，且圆角值一样
 *
 *    2. 使用 riv_roundAsCircle 设置图片为圆形，使用 riv_radius 设置半径，当 riv_radius 未设置时，默认取宽高最小值的一半
 *
 *    3. 使用 riv_topLeft_radius, riv_topRight_radius, riv_bottomLeft_radius, riv_bottomRight_radius 设置4个圆角
 *
 *    4. 使用 riv_borderColor, riv_borderWidth 设置外边框颜色和宽度
 *
 * <p>
 */
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    /** 绘制路径 **/
    private val path = Path()

    /** 绘制坐标 **/
    private val rectF = RectF()

    /** 圆角大小 **/
    private var radius = 0f

    /** 顶部左侧圆角大小 **/
    private var topLeftRadius = 0f

    /** 顶部右侧圆角大小 **/
    private var topRightRadius = 0f

    /** 底部左侧圆角大小 **/
    private var bottomLeftRadius = 0f

    /** 底部右侧圆角大小 **/
    private var bottomRightRadius = 0f

    /** 作为圆形图片使用 **/
    private var roundAsCircle = false

    /** 外边框颜色、宽度、画笔、路径、坐标 */
    private var borderColor = 0
    private var borderWidth = 0f
    private val borderPaint: Paint?
    private val borderPath = Path()
    private val borderRectF = RectF()

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)

        roundAsCircle = ta.getBoolean(R.styleable.RoundImageView_riv_roundAsCircle, false)
        borderColor = ta.getColor(R.styleable.RoundImageView_riv_borderColor, Color.TRANSPARENT)
        borderWidth = ta.getDimension(R.styleable.RoundImageView_riv_borderWidth, 0f)

        radius = ta.getDimension(R.styleable.RoundImageView_riv_radius, 0f)

        topLeftRadius = ta.getDimension(R.styleable.RoundImageView_riv_topLeft_radius, 0f)
        topRightRadius = ta.getDimension(R.styleable.RoundImageView_riv_topRight_radius, 0f)
        bottomLeftRadius = ta.getDimension(R.styleable.RoundImageView_riv_bottomLeft_radius, 0f)
        bottomRightRadius = ta.getDimension(R.styleable.RoundImageView_riv_bottomRight_radius, 0f)

        ta.recycle()

        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        updateBorderPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 当作为圆形图片使用，且半径未设置时，半径将取宽高最小值的一半
        if (roundAsCircle && radius <= 0f) {
            radius = w.coerceAtMost(h) / 2f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 当作为圆形图片使用，宽高值不同，取宽高的最小值作为宽和高
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (roundAsCircle && widthSize > 0 && heightSize > 0 && widthSize != heightSize) {
            val size = widthSize.coerceAtMost(heightSize)
            setMeasuredDimension(size, size)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val halfBorderWidth = borderWidth / 2
        if (radius > 0 || topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
            // 如果设置了圆角值
            path.reset()
            borderPath.reset()
            if (roundAsCircle) {
                path.addCircle(radius, radius, radius, Path.Direction.CW)
            } else {
                if (topLeftRadius == 0f) topLeftRadius = radius
                if (topRightRadius == 0f) topRightRadius = radius
                if (bottomLeftRadius == 0f) bottomLeftRadius = radius
                if (bottomRightRadius == 0f) bottomRightRadius = radius

                val radii = floatArrayOf(
                    topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                    bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
                )

                borderRectF.set(
                    paddingLeft.toFloat() + halfBorderWidth, paddingTop.toFloat() + halfBorderWidth,
                    measuredWidth.toFloat() - paddingRight - halfBorderWidth,
                    measuredHeight.toFloat() - paddingBottom - halfBorderWidth
                )

                borderPath.addRoundRect(borderRectF, radii, Path.Direction.CW)

                if (halfBorderWidth > 0) {
                    radii.forEachIndexed { index, f ->
                        if (f > 0) {
                            radii[index] = f + halfBorderWidth
                        }
                    }
                }
                rectF.set(
                    paddingLeft.toFloat(),
                    paddingTop.toFloat(),
                    measuredWidth.toFloat() - paddingRight,
                    measuredHeight.toFloat() - paddingBottom
                )
                path.addRoundRect(rectF, radii, Path.Direction.CW)
            }

            // 裁剪画布
            canvas.clipPath(path)
        }

        super.onDraw(canvas)

        if (borderWidth > 0 && borderPaint != null) {
            if (roundAsCircle) {
                canvas.drawCircle(radius, radius, radius - borderWidth / 2, borderPaint)
            } else {
                canvas.drawPath(borderPath, borderPaint)
            }
        }
    }

    private fun updateBorderPaint() {
        borderPaint?.apply {
            color = borderColor
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
        }
    }

    /**
     * @param radius 圆角大小，当 asCircle 为 true 时，值作为圆形图片的半径，如果为0，则将取宽高最小值的一半
     * @param borderWidth 外边框宽度
     * @param borderColor 外边框颜色
     * @param asCircle 作为圆形图片使用，默认 false
     */
    fun setRadiusAndBorder(
        radius: Float,
        borderWidth: Float = 0f,
        @ColorInt borderColor: Int = 0,
        asCircle: Boolean = false,
    ) {
        this.radius = radius
        this.borderWidth = borderWidth
        this.borderColor = borderColor
        this.roundAsCircle = asCircle

        updateBorderPaint()
    }

    /**
     * @param topLeftRadius 顶部左侧圆角大小
     * @param topRightRadius 顶部右侧圆角大小
     * @param bottomLeftRadius 底部左侧圆角大小
     * @param bottomRightRadius 底部右侧圆角大小
     * @param borderWidth 外边框宽度
     * @param borderColor 外边框颜色
     */
    fun setRadiusAndBorder(
        topLeftRadius: Float = 0f,
        topRightRadius: Float = 0f,
        bottomLeftRadius: Float = 0f,
        bottomRightRadius: Float = 0f,
        borderWidth: Float = 0f,
        @ColorInt borderColor: Int = 0
    ) {
        this.topLeftRadius = topLeftRadius
        this.topRightRadius = topRightRadius
        this.bottomLeftRadius = bottomLeftRadius
        this.bottomRightRadius = bottomRightRadius
        this.borderWidth = borderWidth
        this.borderColor = borderColor

        updateBorderPaint()
    }

}