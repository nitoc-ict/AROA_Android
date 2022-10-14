package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial.DirectionsApiHelper
import kotlinx.coroutines.launch
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result as Result

class TrialDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val trialRepositoryDummy = TrialRepositoryDummy()

    private val _navFrag = MutableLiveData<Boolean>()
    val navFrag: LiveData<Boolean> get() = _navFrag

    fun navStart() {
        _navFrag.value = true
    }

    fun navCompleted() {
        _navFrag.value = false
        //LiveDataの初期化を行う
    }

    private val _directionsResult = MutableLiveData<DirectionsResult?>()
    val directionsResult: LiveData<DirectionsResult?> = _directionsResult

    private val _origin = MutableLiveData<LatLng>()
    val origin: LiveData<LatLng> get() = _origin

    private val _dest = MutableLiveData<LatLng>()
    val dest: LiveData<LatLng> get() = _dest

    private val _waypoints = MutableLiveData<ArrayList<LatLng>>()
    val waypoints: LiveData<ArrayList<LatLng>> get() = _waypoints

    private val _trial = MutableLiveData<Trial>()
    val trial: LiveData<Trial> get() = _trial

    private val _trialId = MutableLiveData<String>()
    val trialId: LiveData<String> get() = _trialId

    private val _trialPosition = MutableLiveData<LatLng>()
    val trialPosition: LiveData<LatLng> get() = _trialPosition

    private val _trialCourse = MutableLiveData<List<LatLng>>()
    val trialCourse: LiveData<List<LatLng>> get() = _trialCourse

    private val _trialDistance = MutableLiveData<Long>().apply {
        value = 0
    }
    val trialDistance: LiveData<Long> get() = _trialDistance

    fun setTrialId(id: String) {
        _trialId.value = id
    }

    fun getTrialById() {
        viewModelScope.launch {
            trialRepositoryDummy.getTrialById(_trialId.value!!).collect{
                when(it) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _trial.value = it.data!!
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    fun assignmentTrialData() {
        _trial.value?.let {
            when(it) {
                is Trial.Marathon -> {
                    _trialPosition.value = it.position
                    _trialCourse.value = it.course
                }
                else -> {}
            }
        }
    }

    fun getDistance() {
        _trialDistance.value = 0L
        for (route in _directionsResult.value!!.routes) {
            for (leg in route.legs) {
                _trialDistance.value = _trialDistance.value?.plus(leg.distance.inMeters)
            }
        }
    }

    fun courseTranslate() {
        _trialCourse.value?.let { courselatLngs ->
            _origin.value = courselatLngs.first()
            _dest.value = courselatLngs.last()
            _waypoints.value = ArrayList()
            for (latLng in courselatLngs) {
                when(latLng) {
                    courselatLngs.first() -> continue
                    courselatLngs.last() -> continue
                    else -> _waypoints.value?.add(latLng)
                }
            }
        }
    }

    private fun latLngToString(): String {
        val sb = StringBuilder()
        if (_waypoints != null) {
            for (waypoint in _waypoints.value!!) {
                sb.append(waypoint.latitude.toString() + "," + waypoint.longitude.toString() + "|")
            }
        }
        return sb.toString()
    }

    fun directionApiExecute() {
        viewModelScope.launch {
            val waypointsString = latLngToString()
            val result = DirectionsApiHelper().execute(
                com.google.maps.model.LatLng(_origin.value!!.latitude, _origin.value!!.longitude),
                com.google.maps.model.LatLng(_dest.value!!.latitude, _dest.value!!.longitude),
                waypointsString
            )
            _directionsResult.value = result
        }
    }
}