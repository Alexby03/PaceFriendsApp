package com.kth.stepapp.core.utils

import kotlin.math.PI
import kotlin.math.asinh
import kotlin.math.pow
import kotlin.math.tan

object TileMath {

    fun getTileX(lon: Double, zoom: Int): Int {
        val n = 2.0.pow(zoom)
        return (n * ((lon + 180.0) / 360.0)).toInt()
    }

    fun getTileY(lat: Double, zoom: Int): Int {
        val n = 2.0.pow(zoom)
        val latRad = Math.toRadians(lat)

        // Mercator Projection Alg
        val value = (1.0 - asinh(tan(latRad)) / PI) / 2.0
        return (n * value).toInt()
    }
}
