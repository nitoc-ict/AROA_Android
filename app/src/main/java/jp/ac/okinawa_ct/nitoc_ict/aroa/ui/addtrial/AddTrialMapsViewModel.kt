package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.model.DirectionsResult
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class AddTrialMapsViewModel(application: Application) : AndroidViewModel(application) {
    private val _directionsResult = MutableLiveData<DirectionsResult?>()
    val directionsResult: LiveData<DirectionsResult?> = _directionsResult

    private val _origin = MutableLiveData<LatLng>()
    val origin: LiveData<LatLng> get() = _origin

    private val _dest = MutableLiveData<LatLng>()
    val dest: LiveData<LatLng> get() = _dest

    private val _waypoints = MutableLiveData<ArrayList<LatLng>>().apply {
        value = ArrayList()
    }
    val waypoints: LiveData<ArrayList<LatLng>> get() = _waypoints

    private val _waypointMarkers = MutableLiveData<ArrayList<Marker>>().apply {
        value = ArrayList()
    }
    val waypointMarkers: LiveData<ArrayList<Marker>> get() = _waypointMarkers


    fun setOrigin(lat: Double, lng: Double) {
        _origin.value = LatLng(lat,lng)
    }

    fun setDest(lat: Double, lng: Double) {
        _dest.value = LatLng(lat,lng)
    }

    //waypointを追加する
    fun addWaypointMarker(marker: Marker) {
        _waypointMarkers.value?.add(marker)
        directionApiExecute()
    }

    //waypointを削除する
    fun removeWaypointMarker(marker: Marker) {
        _waypointMarkers.value?.remove(marker)
        directionApiExecute()
    }

    //waypointの値を変更する(ドラッグ＆ドロップ)
    fun changeWaypointMarker(marker: Marker) {
        val markersId = ArrayList<String>()
        for (id in _waypointMarkers.value!!) {
            markersId.add(id.id)
        }
        val index = markersId.indexOf(marker.id)
        _waypointMarkers.value?.set(index,marker)
        directionApiExecute()
    }

    //Marker型のwaypointsをDirectionAPIにリクエストを送る用のString型に変更する
    private fun markersToString(): String {
        val sb = StringBuilder()
        if (_waypointMarkers != null) {
            for (waypoint in _waypointMarkers.value!!) {
//                sb.append(waypoint.lat.toString() + "," + waypoint.lng.toString() + "|")
                sb.append(waypoint.position.latitude.toString() + "," + waypoint.position.longitude.toString() + "|")
            }
        }
        val waypointsString = sb.toString()
        return waypointsString
    }


    //DirectionAPIを実行
    fun directionApiExecute() {
        viewModelScope.launch {
            val waypointsString = markersToString()
            val result = DirectionsApiHelper().execute(getApplication(),
                _origin.value, _dest.value, waypointsString
            )
            _directionsResult.value = result
        }
    }
}