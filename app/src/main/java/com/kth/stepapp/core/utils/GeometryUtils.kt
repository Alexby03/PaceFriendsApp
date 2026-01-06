package com.kth.stepapp.core.utils

import android.location.Location
import android.util.Log
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.cos

object GeometryUtils {

    fun calculateArea(points: List<Pair<Double, Double>>): Double {
        if (points.size < 3) return 0.0

        val r = 6378137.0

        val metricPoints = points.map { (lat, lon) ->
            val x = Math.toRadians(lon) * r * cos(Math.toRadians(lat))
            val y = Math.toRadians(lat) * r
            Pair(x, y)
        }

        var area = 0.0
        val n = metricPoints.size
        for (i in 0 until n) {
            val (x1, y1) = metricPoints[i]
            val (x2, y2) = metricPoints[(i + 1) % n]
            area += (x1 * y2) - (x2 * y1)
        }

        return abs(area / 2.0)
    }

    fun calculateDistance(points: List<Pair<Double, Double>>): Float {
        if (points.size < 3) return 999.0f

        val firstPoint = points.first()
        val lastPoint = points.last()

        val results = FloatArray(1)
        Location.distanceBetween(
            firstPoint.first, firstPoint.second,
            lastPoint.first, lastPoint.second,
            results
        )
        val distanceInMeters = results[0]
        Log.d("tryCompleteArea", "distanceInMeters: $distanceInMeters")
        return distanceInMeters
    }

}
