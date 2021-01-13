/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.01.2021, 20:03
 */

package ru.zzemlyanaya.tfood.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import ru.zzemlyanaya.tfood.R
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


open class CTPView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "CircleTPView"

        // Status
        private const val INSTANCE_STATUS = "instance_status"
        private const val STATUS_RADIAN = "status_radian"

        // Default dimension in dp/pt
        private const val PADDINGS = 30f
        private const val DEFAULT_NUMBER_SIZE = 15f
        private const val DEFAULT_TRACK_WIDTH = 16f
        private const val DEFAULT_CIRCLE_BUTTON_RADIUS = 16f
        private const val DEFAULT_CIRCLE_STROKE_WIDTH = 2f

        // Default color
        private const val DEFAULT_CIRCLE_COLOR = R.color.progressGrey
        private const val DEFAULT_CIRCLE_BUTTON_COLOR = R.color.blueDark
        private const val DEFAULT_TRACK_COLOR = R.color.progressGrey
        private const val DEFAULT_PROGRESS_COLOR = R.color.blueDark
    }


    // Paints
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCircleButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrackPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Dimension
    private var paddings = 0f
    private var mNumberSize = 0f
    private var mCircleButtonRadius = 0f
    private var mCircleStrokeWidth = 0f
    private var mTrackWidth = 0f

    @ColorInt
    private var mCircleColor = 0
    @ColorInt
    private var mCircleButtonColor = 0
    @ColorInt
    private var mProgressColor = 0
    @ColorInt
    private var mNumberColor = 0
    @ColorInt
    private var mTrackColor = 0

    // Parameters
    private var mCx = 0f
    private var mCy = 0f
    private var mRadius = 0f
    private var mCurrentRadian = 0f
    private var mCurrentRadian1 = 20f
    private var mPreRadian = 0f
    private var mInCircleButton = false
    private var mInCircleButton1 = false
    private var ismInCircleButton = false
    private var mCurrentTime = 0 // seconds
    private var mListener: OnTimeChangedListener? = null

    private fun initialize() {
        Log.d(TAG, "initialize")
        context.obtainStyledAttributes(
            attrs,
            R.styleable.CTPView,
            defStyleAttr,
            0
        ).use {
            paddings = it.getDimensionPixelSize(
                R.styleable.CTPView_ctpv_paddings,
                dpToPx(PADDINGS).toInt()
            ).toFloat()

            mNumberSize = it.getDimensionPixelSize(
                R.styleable.CTPView_ctpv_numberSize,
                dpToPx(DEFAULT_NUMBER_SIZE).toInt()
            ).toFloat()

            mCircleButtonRadius = it.getDimensionPixelSize(
                R.styleable.CTPView_ctpv_circleButtonRadius,
                dpToPx(DEFAULT_CIRCLE_BUTTON_RADIUS).toInt()
            ).toFloat()

            mCircleStrokeWidth = it.getDimensionPixelSize(
                R.styleable.CTPView_ctpv_circleButtonStrokeWidth,
                dpToPx(DEFAULT_CIRCLE_STROKE_WIDTH).toInt()
            ).toFloat()

            mTrackWidth = it.getDimensionPixelSize(
                    R.styleable.CTPView_ctpv_trackWidth,
                    dpToPx(DEFAULT_TRACK_WIDTH).toInt()
            ).toFloat()

            mCircleColor = it.getColor(
                R.styleable.CTPView_ctpv_circleColor,
                ContextCompat.getColor(context, DEFAULT_CIRCLE_COLOR)
            )
            mCircleButtonColor = it.getColor(
                R.styleable.CTPView_ctpv_circleButtonColor,
                ContextCompat.getColor(context, DEFAULT_CIRCLE_BUTTON_COLOR)
            )
            mProgressColor = it.getColor(
                R.styleable.CTPView_ctpv_progressColor,
                ContextCompat.getColor(context, DEFAULT_PROGRESS_COLOR)
            )
            mNumberColor = it.getColor(
                R.styleable.CTPView_ctpv_numberColor,
                ContextCompat.getColor(context, DEFAULT_PROGRESS_COLOR)
            )
            mTrackColor = it.getColor(
                    R.styleable.CTPView_ctpv_trackColor,
                    ContextCompat.getColor(context, DEFAULT_TRACK_COLOR)
            )
        }

        // CirclePaint
        mCirclePaint.color = mCircleColor
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mCircleStrokeWidth

        // CircleButtonPaint
        mCircleButtonPaint.color = mCircleButtonColor
        mCircleButtonPaint.isAntiAlias = true
        mCircleButtonPaint.style = Paint.Style.FILL

        // ProgressPaint
        mProgressPaint.color = mProgressColor
        mProgressPaint.strokeWidth = mTrackWidth

        // TrackPaint
        mTrackPaint.color = mTrackColor
        mTrackPaint.style = Paint.Style.STROKE
        mTrackPaint.strokeWidth = mTrackWidth

        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    init {
        initialize()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.drawCircle(
            mCx,
            mCy,
            mRadius - mCircleStrokeWidth / 2 - paddings,
            mTrackPaint
        )
        canvas.save()

        if (ismInCircleButton) {
            canvas.rotate(Math.toDegrees(mCurrentRadian.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                mCx,
                measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings,
                mCircleButtonRadius,
                mCircleButtonPaint
            )
            canvas.restore()
            canvas.save()
            canvas.rotate(Math.toDegrees(mCurrentRadian1.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                mCx,
                measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings,
                mCircleButtonRadius,
                mCircleButtonPaint
            )
            canvas.restore()
            canvas.save()
        } else {
            canvas.rotate(Math.toDegrees(mCurrentRadian1.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                mCx,
                measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings,
                mCircleButtonRadius,
                mCircleButtonPaint
            )
            canvas.restore()
            // TimerNumber
            canvas.save()
            canvas.rotate(Math.toDegrees(mCurrentRadian.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                mCx,
                measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings,
                mCircleButtonRadius,
                mCircleButtonPaint
            )
            canvas.restore()
            canvas.save()
        }
        fireTimeOnTimeChangedEvent()
        super.onDraw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and event.actionMasked) {
            MotionEvent.ACTION_DOWN ->
                if (mInCircleButton(event.x, event.y) && isEnabled) {
                    mInCircleButton = true
                    ismInCircleButton = false
                    mPreRadian = getRadian(event.x, event.y)
                    Log.d(TAG, "In circle button")
                } else if (mInCircleButton1(event.x, event.y) && isEnabled) {
                    mInCircleButton1 = true
                    ismInCircleButton = true
                    mPreRadian = getRadian(event.x, event.y)
                }
            MotionEvent.ACTION_MOVE -> if (mInCircleButton && isEnabled) {
                val temp = getRadian(event.x, event.y)
                if (mPreRadian > Math.toRadians(270.0) && temp < Math.toRadians(90.0)) {
                    mPreRadian -= (2 * Math.PI).toFloat()
                } else if (mPreRadian < Math.toRadians(90.0) && temp > Math.toRadians(270.0)) {
                    mPreRadian = (temp + (temp - 2 * Math.PI) - mPreRadian).toFloat()
                }
                mCurrentRadian += temp - mPreRadian
                mPreRadian = temp
                if (mCurrentRadian > 2 * Math.PI) {
                    mCurrentRadian -= (2 * Math.PI).toFloat()
                }
                if (mCurrentRadian < 0) {
                    mCurrentRadian += (2 * Math.PI).toFloat()
                }
                mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadian * 60).toInt()
                invalidate()
            } else if (mInCircleButton1 && isEnabled) {
                val temp = getRadian(event.x, event.y)
                if (mPreRadian > Math.toRadians(270.0) && temp < Math.toRadians(90.0)) {
                    mPreRadian -= (2 * Math.PI).toFloat()
                } else if (mPreRadian < Math.toRadians(90.0) && temp > Math.toRadians(270.0)) {
                    mPreRadian = (temp + (temp - 2 * Math.PI) - mPreRadian).toFloat()
                }
                mCurrentRadian1 += temp - mPreRadian
                mPreRadian = temp
                if (mCurrentRadian1 > 2 * Math.PI) {
                    mCurrentRadian1 -= (2 * Math.PI).toFloat()
                }
                if (mCurrentRadian1 < 0) {
                    mCurrentRadian1 += (2 * Math.PI).toFloat()
                }
                mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadian1 * 60).toInt()
                invalidate()
            }
            MotionEvent.ACTION_UP ->  {
                Log.d(TAG, "ACTION_UP")
                if (mInCircleButton && isEnabled)
                    mInCircleButton = false
                else if (mInCircleButton1 && isEnabled)
                    mInCircleButton1 = false
                mListener?.onMoveEnded(mCurrentTime/360, mCurrentTime/60)
            }
        }
        return true
    }

    private fun fireTimeOnTimeChangedEvent(){
        val i = mCurrentTime / 150
        val minutes = (mCurrentTime - 150 * i) * 10 / 25
        if (ismInCircleButton) {
            mListener?.start(
                    (if (i < 10) "0$i" else i).toString() + ":" + if (minutes < 10) "0$minutes" else minutes)
        } else {
            mListener?.bedTime(
                    (if (i < 10) "0$i" else i).toString() + ":" + if (minutes < 10) "0$minutes" else minutes)
        }
    }

    // Whether the down event inside circle button
    private fun mInCircleButton1(x: Float, y: Float): Boolean {
        val r = mRadius - mCircleStrokeWidth / 2 - paddings
        val x2 = (mCx + r * sin(mCurrentRadian1.toDouble())).toFloat()
        val y2 = (mCy - r * cos(mCurrentRadian1.toDouble())).toFloat()
        return sqrt(((x - x2) * (x - x2) + (y - y2) * (y - y2)).toDouble()) < mCircleButtonRadius
    }

    // Whether the down event inside circle button
    private fun mInCircleButton(x: Float, y: Float): Boolean {
        val r = mRadius - mCircleStrokeWidth / 2 - paddings
        val x2 = (mCx + r * sin(mCurrentRadian.toDouble())).toFloat()
        val y2 = (mCy - r * cos(mCurrentRadian.toDouble())).toFloat()
        return sqrt(((x - x2) * (x - x2) + (y - y2) * (y - y2)).toDouble()) < mCircleButtonRadius
    }

    // Use tri to cal radian
    private fun getRadian(x: Float, y: Float): Float {
        var alpha = atan(((x - mCx) / (mCy - y)).toDouble()).toFloat()
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha += Math.PI.toFloat()
        } else if (x < mCx && y > mCy) {
            // 3
            alpha += Math.PI.toFloat()
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (2 * Math.PI + alpha).toFloat()
        }
        return alpha
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Ensure width = height
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        mCx = (width / 2).toFloat()
        mCy = (height / 2).toFloat()
        // Radius
        mRadius = if (paddings + mCircleStrokeWidth >= mCircleButtonRadius) {
            width / 2 - mCircleStrokeWidth / 2
        } else {
            width / 2 - (mCircleButtonRadius - paddings - mCircleStrokeWidth / 2)
        }
        setMeasuredDimension(width, height)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState())
        bundle.putFloat(STATUS_RADIAN, mCurrentRadian)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATUS))
            mCurrentRadian = state.getFloat(STATUS_RADIAN)
            mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadian * 60).toInt()
            return
        }
        super.onRestoreInstanceState(state)
    }

    fun setOnTimeChangedListener(listener: OnTimeChangedListener?) {
        if (null != listener) {
            mListener = listener
        }
    }

    interface OnTimeChangedListener {
        fun start(starting: String?)
        fun bedTime(ending: String?)
        fun onMoveEnded(hours: Int, minutes: Int)
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )

}