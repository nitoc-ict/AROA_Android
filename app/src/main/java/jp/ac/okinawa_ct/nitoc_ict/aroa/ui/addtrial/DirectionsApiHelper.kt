package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import android.util.Log
import androidx.annotation.Nullable
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.google.maps.model.Unit
import jp.ac.okinawa_ct.nitoc_ict.aroa.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import com.google.maps.model.LatLng as MapsLatLng

class DirectionsApiHelper {
    /**
     * 経路APIを実行する.
     *
     * @param context     コンテキスト
     * @param origin      出発地点
     * @param destination 到着地点
     * @return 取得成功: [com.google.maps.model.DirectionsResult] 失敗: null
     */
    @Nullable
    suspend fun execute(origin: MapsLatLng?, destination: MapsLatLng?, waypoints: String): DirectionsResult? {
        return withContext(Dispatchers.IO) {
            // Mapキーの取得.
            val apiContext = GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY).build()

            // API実行.
            kotlin.runCatching {
                DirectionsApi
                    .newRequest(apiContext)
                    .mode(TravelMode.WALKING)
                    .units(Unit.METRIC)
                    .language(Locale.JAPAN.language)
                    .origin(origin)
                    .destination(destination)
                    .waypoints(waypoints)
                    .await()
            }.getOrNull()
        }
    }

    @Nullable
    suspend fun onlyOriginDestExecute(origin: MapsLatLng?, destination: MapsLatLng?): DirectionsResult? {
        return withContext(Dispatchers.IO) {
            // Mapキーの取得.
            val apiContext = GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY).build()

            Log.i("onlyOriginDestExecute","${origin.toString()},${destination.toString()}")
            // API実行.
            kotlin.runCatching {
                DirectionsApi
                    .newRequest(apiContext)
                    .mode(TravelMode.WALKING)
                    .units(Unit.METRIC)
                    .language(Locale.JAPAN.language)
                    .origin(origin)
                    .destination(destination)
                    .await()
            }.getOrNull()
        }
    }
}