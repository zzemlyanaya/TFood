/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.uikit.circularprogressview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import ru.zzemlyanaya.tfood.R
import kotlin.math.min


class CPVProgressView @JvmOverloads constructor(
        context: Context,
        private val attrs: AttributeSet? = null,
        private val defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "CPVProgressView"

        private const val DEFAULT_MASTER_PROGRESS = 1f
        private const val DEFAULT_STROKE_WIDTH_DP = 12f
        private const val DEFAULT_GAP_WIDTH = 45f
        private const val DEFAULT_GAP_ANGLE = 90f
        private const val DEFAULT_CAP = 1f
        private val DEFAULT_DIRECTION = CPVDirection.CLOCKWISE
        private const val DEFAULT_BG_COLOR_RES = R.color.progressGrey

        private const val DEFAULT_ANIM_ENABLED = true
        private val DEFAULT_INTERPOLATOR = DecelerateInterpolator(1.5f)
        private const val DEFAULT_ANIM_DURATION_MS = 1000
    }

    private var w = 0
    private var h = 0
    private var xPadd = 0f
    private var yPadd = 0f

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f

    /**
     * Percentage of progress shown for all lines.
     *
     * Eg. when one line has 50% of total graph length,
     * setting this to 0.5f will result in that line being animated to 25% of total graph length.
     */
    var masterProgress: Float = DEFAULT_MASTER_PROGRESS
        set(value) {
            if (value !in 0f..1f) {
                return
            }

            field = value

            bgLine.mMasterProgress = value
            lines.forEach { it.mMasterProgress = value }
            invalidate()
        }

    /**
     * Stroke width of all lines in pixels.
     */
    var strokeWidth = dpToPx(DEFAULT_STROKE_WIDTH_DP)
        set(value) {
            field = value

            bgLine.mLineStrokeWidth = value
            lines.forEach { it.mLineStrokeWidth = value }
            updateLinesRadius()
            invalidate()
        }

    /**
     * Stroke cap of all lines.
     */
    var strokeCap = CPVStrokeCap.ROUND
        set(value) {
            field = value

            bgLine.mLineStrokeCap = value
            lines.forEach { it.mLineStrokeCap = value }
            invalidate()
        }

    /**
     * Maximum value of sum of all entries in view, after which
     * all lines start to resize proportionally to amounts in their entry categories.
     */
    var cap: Float = DEFAULT_CAP
        set(value) {
            field = value
            resolveState()
        }

    /**
     * Color of background line.
     */
    @ColorInt
    var bgLineColor: Int = ContextCompat.getColor(context, DEFAULT_BG_COLOR_RES)
        set(value) {
            field = value
            bgLine.mLineColor = value
            invalidate()
        }

    /**
     * Size of gap opening in degrees.
     */
    private var gapWidthDegrees: Float = DEFAULT_GAP_WIDTH
        set(value) {
            field = value

            bgLine.mGapWidthDegrees = value
            lines.forEach { it.mGapWidthDegrees = value }
            invalidate()
        }

    /**
     * Angle in degrees, at which the gap will be displayed.
     */
    private var gapAngleDegrees: Float = DEFAULT_GAP_ANGLE
        set(value) {
            field = value

            bgLine.mGapAngleDegrees = value
            lines.forEach { it.mGapAngleDegrees = value }
            invalidate()
        }

    /**
     * Direction at which view will animate it's progress lines.
     */
    var direction: CPVDirection = DEFAULT_DIRECTION
        set(value) {
            field = value

            bgLine.mDirection = value
            lines.forEach { it.mDirection = value }
            invalidate()
        }

    /**
     * If true, view will animate changes when new data is submitted.
     * If false, state change will happen instantly.
     */
    var animateChanges: Boolean = DEFAULT_ANIM_ENABLED

    /**
     * Interpolator used for state change animations.
     */
    private var animationInterpolator: Interpolator = DEFAULT_INTERPOLATOR

    /**
     * Duration of state change animations.
     */
    var animationDurationMs: Long = DEFAULT_ANIM_DURATION_MS.toLong()

    private val data = mutableListOf<CPVSection>()
    private val lines = mutableListOf<CPVProgressLine>()
    private var animatorSet: AnimatorSet? = null

    private val bgLine = CPVProgressLine(
            name = "_bg",
            radius = radius,
            lineColor = bgLineColor,
            lineStrokeWidth = strokeWidth,
            lineStrokeCap = strokeCap,
            masterProgress = masterProgress,
            length = 1f,
            gapWidthDegrees = gapWidthDegrees,
            gapAngleDegrees = gapAngleDegrees,
            direction = direction
    )

    init {
        obtainAttributes()
    }

    @SuppressLint("Recycle")
    private fun obtainAttributes() {
        context.obtainStyledAttributes(
                attrs,
                R.styleable.CPVProgressView,
                defStyleAttr,
                0
        ).use {
            strokeWidth = it.getDimensionPixelSize(
                    R.styleable.CPVProgressView_CPV_strokeWidth,
                    dpToPx(DEFAULT_STROKE_WIDTH_DP).toInt()
            ).toFloat()

            val strokeCapInt = it.getInt(R.styleable.CPVProgressView_CPV_strokeCap, CPVStrokeCap.ROUND.index)
            val strokeCapNullable = CPVStrokeCap.values().find { enum -> enum.index == strokeCapInt }
            strokeCap = strokeCapNullable ?: error("Unexpected value $strokeCapInt")

            bgLineColor =
                    it.getColor(
                            R.styleable.CPVProgressView_CPV_bgLineColor,
                            ContextCompat.getColor(
                                    context,
                                    DEFAULT_BG_COLOR_RES
                            )
                    )

            gapWidthDegrees =
                    it.getFloat(R.styleable.CPVProgressView_CPV_gapWidth, DEFAULT_GAP_WIDTH)
            gapAngleDegrees =
                    it.getFloat(R.styleable.CPVProgressView_CPV_gapAngle, DEFAULT_GAP_ANGLE)

            direction = CPVDirection.values()[it.getInt(R.styleable.CPVProgressView_CPV_direction, 0)]

            animateChanges = it.getBoolean(
                    R.styleable.CPVProgressView_CPV_animateChanges,
                    DEFAULT_ANIM_ENABLED
            )

            animationDurationMs = it.getInt(
                    R.styleable.CPVProgressView_CPV_animationDuration,
                    DEFAULT_ANIM_DURATION_MS
            ).toLong()

            animationInterpolator =
                    it.getResourceId(R.styleable.CPVProgressView_CPV_animationInterpolator, 0)
                            .let { id ->
                                if (id != 0) {
                                    AnimationUtils.loadInterpolator(context, id)
                                } else {
                                    DEFAULT_INTERPOLATOR
                                }
                            }

            cap = it.getFloat(R.styleable.CPVProgressView_CPV_cap, DEFAULT_CAP)
        }
    }

    /**
     * Returns current data.
     */
    fun getData() = data.toList()

    /**
     * Submits new [sections] to the view.
     *
     * New progress line will be created for each non-existent section and view will be animated to new state.
     * Additionally, existing lines with no data set will be removed when animation completes.
     */
    fun submitData(sections: List<CPVSection>) {
        assertDataConsistency(sections)

        sections
                .filter { it.amount >= 0f }
                .forEach { section ->
                    val newLineColor = section.color
                    if (hasEntriesForSection(section.name).not()) {
                        lines.add(
                                index = 0,
                                element = CPVProgressLine(
                                        name = section.name,
                                        radius = radius,
                                        lineColor = newLineColor,
                                        lineStrokeWidth = strokeWidth,
                                        lineStrokeCap = strokeCap,
                                        masterProgress = masterProgress,
                                        length = 0f,
                                        gapWidthDegrees = gapWidthDegrees,
                                        gapAngleDegrees = gapAngleDegrees,
                                        direction = direction
                                )
                        )
                    } else {
                        lines
                                .filter { it.name == section.name }
                                .forEach { it.mLineColor = newLineColor }
                    }
                }

        this.data.apply {
            val copy = ArrayList(sections)
            clear()
            addAll(copy)
        }

        resolveState()
    }

    /**
     * Adds [amount] to existing section specified by [sectionName]. If section does not exist and [color] is specified,
     * creates new section internally.
     */
    fun addAmount(sectionName: String, amount: Float, color: Int? = null) {
        for (i in 0 until data.size) {
            if (data[i].name == sectionName) {
                data[i] = data[i].copy(amount = data[i].amount + amount)
                submitData(data)
                return
            }
        }

        color?.let {
            submitData(
                    data + CPVSection(
                            name = sectionName,
                            color = it,
                            amount = amount
                    )
            )
        }
                ?: warn {
                    "Adding amount to non-existent section: $sectionName. " +
                            "Please specify color, if you want to have section created automatically."
                }
    }

    /**
     * Sets [amount] for existing section specified by [sectionName].
     * Removes section if amount is <= 0.
     * Does nothing if section does not exist.
     */
    fun setAmount(sectionName: String, amount: Float) {
        for (i in 0 until data.size) {
            if (data[i].name == sectionName) {
                if (amount > 0) {
                    data[i] = data[i].copy(amount = amount)
                } else {
                    data.removeAt(i)
                }
                submitData(data)
                return
            }
        }

        warn { "Setting amount for non-existent section: $sectionName" }
    }

    /**
     * Removes [amount] from existing section specified by [sectionName].
     * If amount gets below zero, removes the section from view.
     */
    fun removeAmount(sectionName: String, amount: Float) {
        for (i in 0 until data.size) {
            if (data[i].name == sectionName) {
                val resultAmount = data[i].amount - amount
                if (resultAmount > 0) {
                    data[i] = data[i].copy(amount = resultAmount)
                } else {
                    data.removeAt(i)
                }
                submitData(data)
                return
            }
        }

        warn { "Removing amount from non-existent section: $sectionName" }
    }

    /**
     * Clear data, removing all lines.
     */
    fun clear() = submitData(listOf())

    private fun assertDataConsistency(data: List<CPVSection>) {
        if (data.hasDuplicatesBy { it.name }) {
            throw IllegalStateException("Multiple sections with same name found")
        }
    }

    private fun resolveState() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet()

        val sectionAmounts = lines.map { getAmountForSection(it.name) }
        val totalAmount = sectionAmounts.sumByFloat { it }

        val drawPercentages = sectionAmounts.mapIndexed { index, _ ->
            if (totalAmount > cap) {
                getDrawAmountForLine(sectionAmounts, index) / totalAmount
            } else {
                getDrawAmountForLine(sectionAmounts, index) / cap
            }
        }

        drawPercentages.forEachIndexed { index, newPercentage ->
            val line = lines[index]
            val animator = animateLine(line, newPercentage) {
                if (!hasEntriesForSection(line.name)) {
                    removeLine(line)
                }
            }

            animatorSet?.play(animator)
        }

        animatorSet?.start()
    }

    private fun getAmountForSection(sectionName: String): Float {
        return data
                .filter { it.name == sectionName }
                .sumByFloat { it.amount }
    }

    private fun getDrawAmountForLine(amounts: List<Float>, index: Int): Float {
        if (index >= amounts.size) {
            return 0f
        }

        val thisLine = amounts[index]
        val previousLine = getDrawAmountForLine(amounts, index + 1) // Length of line above this one

        return thisLine + previousLine
    }

    private fun hasEntriesForSection(section: String) =
            data.indexOfFirst { it.name == section } > -1

    private fun animateLine(
            line: CPVProgressLine,
            to: Float,
            animationEnded: (() -> Unit)? = null
    ): ValueAnimator {
        return ValueAnimator.ofFloat(line.mLength, to).apply {
            duration = if (animateChanges) animationDurationMs else 0L
            interpolator = animationInterpolator
            addUpdateListener {
                (it.animatedValue as? Float)?.let { animValue ->
                    line.mLength = animValue
                }
                invalidate()
            }

            doOnEnd {
                animationEnded?.invoke()
            }
        }
    }

    private fun removeLine(line: CPVProgressLine) {
        lines.remove(line)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.w = w
        this.h = h

        this.xPadd = (paddingLeft + paddingRight).toFloat()
        this.yPadd = (paddingTop + paddingBottom).toFloat()

        this.centerX = w / 2f
        this.centerY = h / 2f

        updateLinesRadius()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)

        super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY)
        )
    }

    private fun updateLinesRadius() {
        val ww = w.toFloat() - xPadd
        val hh = h.toFloat() - yPadd
        this.radius = min(ww, hh) / 2f - strokeWidth / 2f

        bgLine.mRadius = radius
        lines.forEach { it.mRadius = radius }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(centerX, centerY)

        bgLine.draw(canvas)
        lines.forEach { it.draw(canvas) }
    }

    private fun dpToPx(dp: Float) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
    )

    private fun warn(text: () -> String) {
        Log.w(TAG, text())
    }
}