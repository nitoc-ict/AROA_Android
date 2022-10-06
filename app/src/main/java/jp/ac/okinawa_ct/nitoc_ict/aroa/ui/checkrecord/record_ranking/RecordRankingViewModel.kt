package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import kotlinx.coroutines.launch

class RecordRankingViewModel : ViewModel() {
    private val recordRepositoryDummy = RecordRepositoryDummy()

    private val _testRecordData = MutableLiveData<List<Record>>()
    val testRecordData: LiveData<List<Record>> get() = _testRecordData

    private val _trialId = MutableLiveData<String>()
    val trialId: LiveData<String> get() = _trialId

    fun setTrialId(id: String) {
        _trialId.value = id
    }

    fun getRanking(trialId: String) {
        viewModelScope.launch {
            recordRepositoryDummy.getRecords(trialId,1,10).collect{
                when(it) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _testRecordData.value = it.data!!
                    }
                    is Result.Error -> {}
                }
            }
        }
    }

    private fun sortRanking() {

    }


}