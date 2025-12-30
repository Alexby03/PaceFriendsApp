package com.kth.stepapp.core.utils

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
}
