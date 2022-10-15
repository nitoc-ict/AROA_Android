package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.check_record.record_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial.DirectionsApiHelper
import kotlinx.coroutines.launch

class RecordDetailViewModel : ViewModel() {
    private val trialRepositoryDummy = TrialRepositoryDummy()
    private val recordRepositoryDummy = RecordRepositoryDummy()

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

    private val _recordId = MutableLiveData<String>()
    val recordId: LiveData<String> get() = _recordId

    private val _record = MutableLiveData<Record>()
    val record: LiveData<Record> get() = _record

    private val _recordTime = MutableLiveData<Long>()
    val recordTime: LiveData<Long> get() = _recordTime

    private val _trial = MutableLiveData<Trial>()
    val trial: LiveData<Trial> get() = _trial

    private val _trialId = MutableLiveData<String>()
    val trialId: LiveData<String> get() = _trialId

    private val _trialCourse = MutableLiveData<List<LatLng>>()
    val trialCourse: LiveData<List<LatLng>> get() = _trialCourse

    private val _trialDistance = MutableLiveData<Long>().apply {
        value = 0
    }
    val trialDistance: LiveData<Long> get() = _trialDistance

    fun setTrialId(id: String) {
        _trialId.value = id
    }

    fun setRecordId(id: String) {
        _recordId.value = id
    }

    fun getTrialById() {
        viewModelScope.launch {
            trialRepositoryDummy.getTrialById(_trialId.value!!).collect{
                when(it) {
                    is Result.Loading -> "Loading"
                    is Result.Success -> {
                        _trial.value = it.data!!
                    }
                    is Result.Error -> "Error"
                }
            }
        }
    }

    fun getRecordById() {
        viewModelScope.launch {
            recordRepositoryDummy.getRecordByTrialIdAndRecordId(trialId.value!!, _recordId.value!!).collect{
                when(it) {
                    is Result.Loading -> "Loading"
                    is Result.Success -> {
                        _record.value = it.data!!
                    }
                    is Result.Error -> "Error"
                }
            }
        }
    }

    fun assignmentTrialData() {
        _trial.value?.let {
            when(it) {
                is Trial.Marathon -> {
                    _trialCourse.value = it.course
                }
                else -> {}
            }
        }
    }

    fun assignmentRecordDate() {
        _record.value?.let {
            when(it) {
                is Record.MarathonRecord -> {
                    _recordTime.value = it.time
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
        _trialCourse.value?.let { courselatLng ->
            _origin.value = courselatLng.first()
            _dest.value = courselatLng.last()
            _waypoints.value = ArrayList()
            for (latLng in courselatLng) {
                when(latLng) {
                    courselatLng.first() -> continue
                    courselatLng.last() -> continue
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