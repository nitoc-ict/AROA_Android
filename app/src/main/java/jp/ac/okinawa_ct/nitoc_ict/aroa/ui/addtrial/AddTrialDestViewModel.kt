package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng as MapsLatLng
import kotlinx.coroutines.launch

class AddTrialDestViewModel(application: Application) : AndroidViewModel(application) {
    private val _directionsResult = MutableLiveData<DirectionsResult?>()
    val directionsResult: LiveData<DirectionsResult?> = _directionsResult

    private val _origin = MutableLiveData<LatLng>()
    val origin: LiveData<LatLng> get() = _origin

    private val _dest = MutableLiveData<LatLng>()
    val dest: LiveData<LatLng> get() = _dest

    private val _navFrag = MutableLiveData<Boolean>()
    val navFrag: LiveData<Boolean> get() = _navFrag

    fun setOrigin(latLng: LatLng) {
        _origin.value = latLng
    }

    fun setDest(latLng: LatLng) {
        Log.i("DestViewModel","setDest:${latLng.toString()}")
        _dest.value = latLng
        directionApiExecute()
    }

    fun removeDest() {
        if (_dest != null) {
//            _dest.value = null
        }
    }

    fun navStart() {
        _navFrag.value = true
    }

    fun navCompleted() {
        _navFrag.value = false
    }

    //DirectionAPIを実行
    fun directionApiExecute() {
        viewModelScope.launch {
            val result = DirectionsApiHelper().onlyOriginDestExecute(
                MapsLatLng(_origin.value!!.latitude, _origin.value!!.longitude),
                MapsLatLng(_dest.value!!.latitude, _dest.value!!.longitude)
            )
            Log.i("DestViewModel","result:${result.toString()}")
            _directionsResult.value = result
        }
    }
}