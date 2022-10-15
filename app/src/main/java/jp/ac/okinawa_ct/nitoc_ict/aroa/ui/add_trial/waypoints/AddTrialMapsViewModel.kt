package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial.waypoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial.DirectionsApiHelper
import kotlinx.coroutines.launch

class AddTrialMapsViewModel(application: Application) : AndroidViewModel(application) {
    private val _directionsResult = MutableLiveData<DirectionsResult?>()
    val directionsResult: LiveData<DirectionsResult?> = _directionsResult

    private val trialRepositoryDummy = TrialRepositoryDummy()

    private val _navFrag = MutableLiveData<Boolean>()
    val navFrag: LiveData<Boolean> get() = _navFrag

    private val _origin = MutableLiveData<LatLng>()
    val origin: LiveData<LatLng> get() = _origin

    private val _dest = MutableLiveData<LatLng>()
    val dest: LiveData<LatLng> get() = _dest

    private val _waypointMarkers = MutableLiveData<ArrayList<Marker>>().apply {
        value = ArrayList()
    }
    val waypointMarkers: LiveData<ArrayList<Marker>> get() = _waypointMarkers

    private val _trialCourse = MutableLiveData<ArrayList<LatLng>>().apply {
        value = ArrayList()
    }
    val trialCourse: LiveData<ArrayList<LatLng>> get() = _trialCourse

    private val _trialName = MutableLiveData<String>()
    val trialName: LiveData<String> get() = _trialName

    fun setTrialName(name: String) {
        _trialName.value = name
    }

    fun setOrigin(latLng: LatLng) {
        _origin.value = latLng
    }

    fun setDest(latLng: LatLng) {
        _dest.value = latLng
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
        return sb.toString()
    }


    //DirectionAPIを実行
    fun directionApiExecute() {
        viewModelScope.launch {
            if (_waypointMarkers.value!!.isNotEmpty()) {
                val waypointsString = markersToString()
                val result = DirectionsApiHelper().execute(
                    com.google.maps.model.LatLng(_origin.value!!.latitude, _origin.value!!.longitude),
                    com.google.maps.model.LatLng(_dest.value!!.latitude, _dest.value!!.longitude),
                    waypointsString
                )
                _directionsResult.value = result
            }else {
                val result = DirectionsApiHelper().onlyOriginDestExecute(
                    com.google.maps.model.LatLng(_origin.value!!.latitude, _origin.value!!.longitude),
                    com.google.maps.model.LatLng(_dest.value!!.latitude, _dest.value!!.longitude),
                )
                _directionsResult.value = result
            }

        }
    }

    fun createNewTrial() {
        _trialCourse.value!!.add(_origin.value!!)
        for (waypoint in _waypointMarkers.value!!) {
            _trialCourse.value!!.add(LatLng(waypoint.position.latitude,waypoint.position.longitude))
        }
        _trialCourse.value!!.add(_dest.value!!)
        val trial = Trial.Marathon(_trialName.value!!,
            "ひじかた",
            _trialCourse.value!!.toList()[0],
            _trialCourse.value!!.toList(),
            "2022年10月16日")
        viewModelScope.launch {
            trialRepositoryDummy.createTrial(trial).collect{
                when(it) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        //navigationを実装
                        _navFrag.value = true
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    fun navCompleted() {
        _navFrag.value = false
    }
}