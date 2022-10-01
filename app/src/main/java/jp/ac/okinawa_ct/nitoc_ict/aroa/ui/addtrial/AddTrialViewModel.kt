package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTrialViewModel : ViewModel() {
    private val _navFrag = MutableLiveData<Boolean?>()
    val navFrag: LiveData<Boolean?> get() = _navFrag

    fun navStart() {
        _navFrag.value = true
    }

    fun navCompleted() {
        _navFrag.value = null
    }
}