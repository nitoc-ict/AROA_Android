package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.check_record.record_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import kotlinx.coroutines.launch
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result as Result

class CheckRecordListViewModel : ViewModel() {
    private val recordRepositoryDummy = RecordRepositoryDummy()

    private val _testRecordList = MutableLiveData<List<Record>>()
    val testRecordList: LiveData<List<Record>> get() = _testRecordList

    private val _collectState = MutableLiveData<String>()
    val collectState: LiveData<String> get() = _collectState

    init {
        getRecordList()
    }

    private fun getRecordList() {
        viewModelScope.launch {
            recordRepositoryDummy.getMyRecords("").collect{
                when(it) {
                    is Result.Loading -> _collectState.value = "Loading"
                    is Result.Success -> {
                        _testRecordList.value = it.data!!
                    }
                    is Result.Error -> _collectState.value = "Error"
                }
            }
        }
    }

}