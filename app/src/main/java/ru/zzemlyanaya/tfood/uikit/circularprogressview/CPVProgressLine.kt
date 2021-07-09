/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.uikit.circularprogressview

import android.graphics.*
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin


internal class CPVProgressLine(
        val name: String,
        radius: Float,
        lineColor: Int,
        lineStrokeWidth: Float,
        lineStrokeCap: CPVStrokeCap,
        masterProgress: Float,
        length: Float,
        gapWidthDegrees: Float,
        gapAngleDegrees: Float,
        direction: CPVDirection
) {

    companion object {
        const val SIDES = 64
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = lineStrokeCap.cap
        strokeWidth = mLineStrokeWidth
        color = mLineColor
    }

    var mRadius: Float = 0.0f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mLineColor: Int = 0
        set(value) {
            field = value
            paint.color = value
        }

    var mLineStrokeWidth: Float = 0.0f
        set(value) {
            field = value
            paint.strokeWidth = value
        }

    var mLineStrokeCap: CPVStrokeCap = CPVStrokeCap.ROUND
        set(value) {
            field = value
            paint.strokeCap = value.cap
        }

    var mMasterProgress: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var mLength: Float = 0.0f
        set(value) {
            field = value
            updatePathEffect()
        }

    var mGapWidthDegrees = 10f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mGapAngleDegrees = 270f
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    var mDirection = CPVDirection.CLOCKWISE
        set(value) {
            field = value
            updatePath()
            updatePathEffect()
        }

    private var path: Path = createPath()

    init {
        this.mRadius = radius
        this.mLineColor = lineColor
        this.mLineStrokeWidth = lineStrokeWidth
        this.mLineStrokeCap = lineStrokeCap
        this.mMasterProgress = masterProgress
        this.mLength = length
        this.mGapWidthDegrees = gapWidthDegrees
        this.mGapAngleDegrees = gapAngleDegrees
        this.mDirection = direction
    }

    private fun createPath(): Path {
        val path = Path()

        val offset = mGapAngleDegrees.toRadians()

        val startAngle = when (mDirection) {
            CPVDirection.CLOCKWISE -> 0.0 + (mGapWidthDegrees / 2f).toRadians()
            CPVDirection.ANTICLOCKWISE -> Math.PI * 2.0 - (mGapWidthDegrees / 2f).toRadians()
        }
        val endAngle = when (mDirection) {
            CPVDirection.CLOCKWISE -> Math.PI * 2.0 - (mGapWidthDegrees / 2f).toRadians()
            CPVDirection.ANTICLOCKWISE -> 0.0 + (mGapWidthDegrees / 2f).toRadians()
        }
        val angleStep = (endAngle - startAngle) / SIDES

        path.moveTo(
                mRadius * cos(startAngle + offset).toFloat(),
                mRadius * sin(startAngle + offset).toFloat()
        )

        for (i in 1 until SIDES + 1) {
            path.lineTo(
                    mRadius * cos(i * angleStep + offset + startAngle).toFloat(),
                    mRadius * sin(i * angleStep + offset + startAngle).toFloat()
            )
        }

        return path
    }

    private fun updatePath() {
        this.path = createPath()
    }

    private fun updatePathEffect() {
        val pathLen = PathMeasure(path, false).length
        val drawnLength = ceil(pathLen.toDouble() * mLength * mMasterProgress).toFloat()

        paint.pathEffect = ComposePathEffect(
                CornerPathEffect(pathLen / SIDES),
                DashPathEffect(
                        floatArrayOf(
                                drawnLength,
                                pathLen - drawnLength
                        ),
                        0f
                )
        )
    }

    fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

}