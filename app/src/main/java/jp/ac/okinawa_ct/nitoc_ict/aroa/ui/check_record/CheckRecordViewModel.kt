package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.check_record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckRecordViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is CheckRecord Fragment"
    }
    val text: LiveData<String> = _text
}