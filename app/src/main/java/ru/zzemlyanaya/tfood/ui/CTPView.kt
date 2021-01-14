/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 15:40
 */

package ru.zzemlyanaya.tfood.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
        private const val DEFAULT_NUMBER_SIZE = 10f
        private const val DEFAULT_TRACK_WIDTH = 16f
        private const val DEFAULT_CIRCLE_BUTTON_RADIUS = 18f
        private const val DEFAULT_CIRCLE_STROKE_WIDTH = 1f

        // Default color
        private const val DEFAULT_CIRCLE_COLOR = R.color.progressGrey
        private const val DEFAULT_CIRCLE_BUTTON_COLOR = R.color.blueDark
        private const val DEFAULT_TRACK_COLOR = R.color.progressGrey
        private const val DEFAULT_PROGRESS_COLOR = R.color.blueDark
        private const val DEFAULT_ICONS_COLOR = R.color.white
    }


    // Paints
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCircleButtonBedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCircleButtonWakePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mIconsPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Dimension
    private var paddings = 0f
    private var mNumberSize = 0f
    private var mCircleButtonRadius = 0f
    private var mCircleStrokeWidth = 0f
    private var mTrackWidth = 0f

    @ColorInt
    private var mCircleColor = 0
    @ColorInt
    private var mCircleButtonBedColor = 0
    @ColorInt
    private var mCircleButtonWakeColor = 0
    @ColorInt
    private var mProgressColor = 0
    @ColorInt
    private var mNumberColor = 0
    @ColorInt
    private var mTrackColor = 0
    @ColorInt
    private var mIconsColor = 0

    // Parameters
    private var mCx = 0f
    private var mCy = 0f
    private var mRadius = 0f
    private var mCurrentRadianBed = 0f
    private var mCurrentRadianWake = 0f
    private var mPreRadian = 0f
    private var mInCircleButtonBed = false
    private var mInCircleButtonWake = false
    private var ismInCircleButtonBed = false
    private var mCurrentTime = 0 // seconds
    private var mListener: OnTimeChangedListener? = null

    // Icons
    private lateinit var moonIcon: Bitmap
    private lateinit var moonBitmap: Bitmap
    private lateinit var dawnIcon: Bitmap
    private lateinit var dawnBitmap: Bitmap

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
            mCircleButtonBedColor = it.getColor(
                    R.styleable.CTPView_ctpv_circleButtonBedColor,
                    ContextCompat.getColor(context, DEFAULT_CIRCLE_BUTTON_COLOR)
            )
            mCircleButtonWakeColor = it.getColor(
                    R.styleable.CTPView_ctpv_circleButtonWakeColor,
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
            mIconsColor = it.getColor(
                    R.styleable.CTPView_ctpv_iconsColor,
                    ContextCompat.getColor(context, DEFAULT_ICONS_COLOR)
            )
        }

        moonIcon = drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_moon)!!)
        moonBitmap = createScaledBitmap(
                moonIcon,
                mCircleButtonRadius.toInt(),
                mCircleButtonRadius.toInt(),
                false)
        dawnIcon = drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_sunrise)!!)
        dawnBitmap = createScaledBitmap(
                dawnIcon,
                mCircleButtonRadius.toInt(),
                mCircleButtonRadius.toInt(),
                false)

        // CirclePaint
        mCirclePaint.color = mCircleColor
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mCircleStrokeWidth

        // CircleButtonWakePaint
        mCircleButtonBedPaint.color = mCircleButtonWakeColor
        mCircleButtonBedPaint.isAntiAlias = true
        mCircleButtonBedPaint.style = Paint.Style.FILL

        // CircleButtonBedPaint
        mCircleButtonWakePaint.color = mCircleButtonBedColor
        mCircleButtonWakePaint.isAntiAlias = true
        mCircleButtonWakePaint.style = Paint.Style.FILL

        // ProgressPaint
        mProgressPaint.color = mProgressColor
        mProgressPaint.strokeWidth = mTrackWidth
        mProgressPaint.style = Paint.Style.STROKE

        // TrackPaint
        mTrackPaint.color = mTrackColor
        mTrackPaint.style = Paint.Style.STROKE
        mTrackPaint.strokeWidth = mTrackWidth

        // IconsPaint
        mIconsPaint.color = mIconsColor
        mIconsPaint.style = Paint.Style.STROKE

        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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

        canvas.rotate(-90f, mCx, mCy)

        val rect = RectF(
                mCx - (mRadius - mCircleStrokeWidth / 2 - paddings),
                mCy - (mRadius - mCircleStrokeWidth / 2 - paddings),
                mCx + (mRadius - mCircleStrokeWidth / 2 - paddings),
                mCy + (mRadius - mCircleStrokeWidth / 2 - paddings))

        if (mCurrentRadianWake > mCurrentRadianBed) {
            canvas.drawArc(
                    rect,
                    Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat(),
                    Math.toDegrees((2 * Math.PI.toFloat()).toDouble()).toFloat() - Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat() + Math.toDegrees(mCurrentRadianBed.toDouble()).toFloat(),
                    false, mProgressPaint
            )
        } else {
            canvas.drawArc(
                    rect,
                    Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat(),
                    Math.toDegrees(mCurrentRadianBed.toDouble()).toFloat() - Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat(),
                    false, mProgressPaint
            )
        }
        canvas.restore()
        canvas.save()

        if (ismInCircleButtonBed) {
            canvas.rotate(Math.toDegrees(mCurrentRadianBed.toDouble()).toFloat(), mCx, mCy)
            val butY = measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings
            canvas.drawCircle(
                    mCx,
                    butY,
                    mCircleButtonRadius,
                    mCircleButtonBedPaint
            )
            canvas.drawBitmap(dawnBitmap, mCx-mCircleButtonRadius/2, butY-mCircleButtonRadius/2, mIconsPaint)
            canvas.restore()
            canvas.save()
            canvas.rotate(Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                    mCx,
                    butY,
                    mCircleButtonRadius,
                    mCircleButtonWakePaint
            )
            canvas.drawBitmap(moonBitmap, mCx-mCircleButtonRadius/2, butY-mCircleButtonRadius/2, mIconsPaint)
            canvas.restore()
            canvas.save()
        } else {
            canvas.rotate(Math.toDegrees(mCurrentRadianWake.toDouble()).toFloat(), mCx, mCy)
            val butY = measuredHeight / 2 - mRadius + mCircleStrokeWidth / 2 + paddings
            canvas.drawCircle(
                    mCx,
                    butY,
                    mCircleButtonRadius,
                    mCircleButtonWakePaint
            )
            canvas.drawBitmap(moonBitmap, mCx-mCircleButtonRadius/2, butY-mCircleButtonRadius/2, mIconsPaint)
            canvas.restore()
            canvas.save()
            canvas.rotate(Math.toDegrees(mCurrentRadianBed.toDouble()).toFloat(), mCx, mCy)
            canvas.drawCircle(
                    mCx,
                    butY,
                    mCircleButtonRadius,
                    mCircleButtonBedPaint
            )
            canvas.drawBitmap(dawnBitmap, mCx-mCircleButtonRadius/2, butY-mCircleButtonRadius/2, mIconsPaint)
            canvas.restore()
            canvas.save()
        }
        fireTimeOnTimeChangedEvent()
        super.onDraw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and event.actionMasked) {
            MotionEvent.ACTION_DOWN ->
                if (mInCircleButtonBed(event.x, event.y) && isEnabled) {
                    mInCircleButtonBed = true
                    ismInCircleButtonBed = false
                    mPreRadian = getRadian(event.x, event.y)
                } else if (mInCircleButtonWake(event.x, event.y) && isEnabled) {
                    mInCircleButtonWake = true
                    ismInCircleButtonBed = true
                    mPreRadian = getRadian(event.x, event.y)
                }
            MotionEvent.ACTION_MOVE -> if (mInCircleButtonBed && isEnabled) {
                val temp = getRadian(event.x, event.y)
                if (mPreRadian > Math.toRadians(270.0) && temp < Math.toRadians(90.0)) {
                    mPreRadian -= (2 * Math.PI).toFloat()
                } else if (mPreRadian < Math.toRadians(90.0) && temp > Math.toRadians(270.0)) {
                    mPreRadian = (temp + (temp - 2 * Math.PI) - mPreRadian).toFloat()
                }
                mCurrentRadianBed += temp - mPreRadian
                mPreRadian = temp
                if (mCurrentRadianBed > 2 * Math.PI) {
                    mCurrentRadianBed -= (2 * Math.PI).toFloat()
                }
                if (mCurrentRadianBed < 0) {
                    mCurrentRadianBed += (2 * Math.PI).toFloat()
                }
                mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadianBed * 60).toInt()
                invalidate()
            } else if (mInCircleButtonWake && isEnabled) {
                val temp = getRadian(event.x, event.y)
                if (mPreRadian > Math.toRadians(270.0) && temp < Math.toRadians(90.0)) {
                    mPreRadian -= (2 * Math.PI).toFloat()
                } else if (mPreRadian < Math.toRadians(90.0) && temp > Math.toRadians(270.0)) {
                    mPreRadian = (temp + (temp - 2 * Math.PI) - mPreRadian).toFloat()
                }
                mCurrentRadianWake += temp - mPreRadian
                mPreRadian = temp
                if (mCurrentRadianWake > 2 * Math.PI) {
                    mCurrentRadianWake -= (2 * Math.PI).toFloat()
                }
                if (mCurrentRadianWake < 0) {
                    mCurrentRadianWake += (2 * Math.PI).toFloat()
                }
                mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadianWake * 60).toInt()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP")
                if (mInCircleButtonBed && isEnabled)
                    mInCircleButtonBed = false
                else if (mInCircleButtonWake && isEnabled)
                    mInCircleButtonWake = false
                mListener?.onMoveEnded()
            }
        }
        return true
    }

    private fun fireTimeOnTimeChangedEvent(){
        val i = mCurrentTime / 150
        val minutes = (mCurrentTime - 150 * i) * 10 / 25
        if (ismInCircleButtonBed) {
            mListener?.bedTime(
                    (if (i < 10) "0$i" else i).toString() + ":" + if (minutes < 10) "0$minutes" else minutes, i * 60 + minutes)
        } else {
            mListener?.wakeTime(
                    (if (i < 10) "0$i" else i).toString() + ":" + if (minutes < 10) "0$minutes" else minutes, i * 60 + minutes)
        }
    }

    // Whether the down event inside circle button
    private fun mInCircleButtonWake(x: Float, y: Float): Boolean {
        val r = mRadius - mCircleStrokeWidth / 2 - paddings
        val x2 = (mCx + r * sin(mCurrentRadianWake.toDouble())).toFloat()
        val y2 = (mCy - r * cos(mCurrentRadianWake.toDouble())).toFloat()
        return sqrt(((x - x2) * (x - x2) + (y - y2) * (y - y2)).toDouble()) < mCircleButtonRadius
    }

    // Whether the down event inside circle button
    private fun mInCircleButtonBed(x: Float, y: Float): Boolean {
        val r = mRadius - mCircleStrokeWidth / 2 - paddings
        val x2 = (mCx + r * sin(mCurrentRadianBed.toDouble())).toFloat()
        val y2 = (mCy - r * cos(mCurrentRadianBed.toDouble())).toFloat()
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
        bundle.putFloat(STATUS_RADIAN, mCurrentRadianBed)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATUS))
            mCurrentRadianBed = state.getFloat(STATUS_RADIAN)
            mCurrentTime = (60 / (2 * Math.PI) * mCurrentRadianBed * 60).toInt()
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
        fun bedTime(starting: String?, bedTime: Int)
        fun wakeTime(ending: String?, wakeTime: Int)
        fun onMoveEnded()
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
    )

}