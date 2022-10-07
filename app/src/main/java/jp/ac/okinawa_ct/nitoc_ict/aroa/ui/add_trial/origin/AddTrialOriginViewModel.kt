package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.origin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

class AddTrialOriginViewModel(application: Application) : AndroidViewModel(application) {
    private val _origin = MutableLiveData<LatLng>()
    val origin: LiveData<LatLng> get() = _origin

    private val _navFrag = MutableLiveData<Boolean>()
    val navFrag: LiveData<Boolean> get() = _navFrag

    fun setOrigin(latLng: LatLng) {
        _origin.value = latLng
    }

    fun removeOrigin() {
        if (_origin != null) {
//            _origin.value = null
        }
    }

    fun navStart() {
        _navFrag.value = true
    }

    fun navCompleted() {
        _navFrag.value = false
    }
}