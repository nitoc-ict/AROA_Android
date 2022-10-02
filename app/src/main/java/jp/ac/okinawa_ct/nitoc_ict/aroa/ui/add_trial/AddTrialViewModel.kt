package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTrialViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is addTrial Fragment"
    }
    val text: LiveData<String> = _text
}