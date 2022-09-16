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


    init {
        _origin.value = LatLng(26.387409, 127.729753)
        _dest.value = LatLng(26.387409, 127.729753)
//        _dest.value = LatLng(26.52486985103984, 128.0300908585622)
//        _waypoints.value?.add(LatLng(26.313224610295485, 127.79608257031921))
    }

    fun setOrigin(lat: Double, lng: Double) {
        _origin.value = LatLng(lat,lng)
    }

    fun setDest(lat: Double, lng: Double) {
        _dest.value = LatLng(lat,lng)
    }


    fun addWaypointMarker(marker: Marker) {
        _waypointMarkers.value?.add(marker)
        Log.i("MapsActivity", "addWaypointMarker:${_waypointMarkers.value.toString()}")
        execute()
    }

    fun removeWaypointMarker(marker: Marker) {
        Log.i("MapsActivity","removeMarkerLatLng:${marker.position}")
        _waypointMarkers.value?.remove(marker)
        Log.i("MapsActivity", "removeWaypointMarker:${_waypointMarkers.value.toString()}")
        execute()
    }

    fun changeWaypointMarker(marker: Marker) {
        val markersId = ArrayList<String>()
        for (id in _waypointMarkers.value!!) {
            markersId.add(id.id)
        }
        val index = markersId.indexOf(marker.id)
        _waypointMarkers.value?.set(index,marker)
        Log.i("MapsActivity", "changeWaypointMarker:${_waypointMarkers.value!!.get(index).position}")
        execute()
    }

    private fun markersToString(): String {
        Log.i("MapsActivity","do1:")
        val sb = StringBuilder()
        Log.i("MapsActivity","do2:")
        if (_waypointMarkers != null) {
            Log.i("MapsActivity","do3:")
            for (waypoint in _waypointMarkers.value!!) {
//                sb.append(waypoint.lat.toString() + "," + waypoint.lng.toString() + "|")
                Log.i("MapsActivity","waypointsString: ${waypoint.position.latitude.toString() + "," + waypoint.position.longitude.toString() + "|"}")
                sb.append(waypoint.position.latitude.toString() + "," + waypoint.position.longitude.toString() + "|")
            }
        }
        val waypointsString = sb.toString()
//            waypointsString.drop(1)
//            waypointsString.dropLast(2)
//            waypointsString.replace("[", "").replace("]", "")
        Log.i("MapsActivity","waypointsString: $waypointsString")
        return waypointsString
    }



    fun execute() {
        viewModelScope.launch {
            val waypointsString = markersToString()
            Log.i("MapsActivity", "execute:${_waypoints.value.toString()}")
            val result = DirectionsApiHelper().execute(getApplication(),
                _origin.value, _dest.value, waypointsString
            )
            Log.i("MapsActivity", "executed:${result.toString()}")
            _directionsResult.value = result
        }
    }
}