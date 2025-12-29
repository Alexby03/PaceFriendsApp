package com.kth.stepapp.data.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.kth.stepapp.core.api.RetrofitClient
import com.kth.stepapp.core.utils.TileMath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MapRepository {

    private val _map: MutableMap<String, Bitmap> = mutableMapOf()
    val map = _map.toMap()

    private fun coordsToTile(lat: Double, lng: Double, zoom: Int): String {
        val tileX = TileMath.getTileX(lng, zoom)
        val tileY = TileMath.getTileY(lat, zoom)
        return "$zoom-$tileX-$tileY"
    }

    suspend fun getMapTile(lat: Double, lng: Double, zoom: Int): Bitmap? {
        val tilesKey = coordsToTile(lat, lng, zoom)
        if (_map.containsKey(tilesKey)) {
            Log.d("Map", "Found tiles: $tilesKey")
            return _map[tilesKey]
        }
        return try {
            withContext(Dispatchers.IO) {
                val tileX = TileMath.getTileX(lng, zoom)
                val tileY = TileMath.getTileY(lat, zoom)
                val response = RetrofitClient.osmService.getMapTile(zoom, tileX, tileY)
                if (response.isSuccessful) {
                    val bitMap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    _map[tilesKey] = bitMap
                    bitMap
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("MapRepository", "Error fetching tile: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }
}