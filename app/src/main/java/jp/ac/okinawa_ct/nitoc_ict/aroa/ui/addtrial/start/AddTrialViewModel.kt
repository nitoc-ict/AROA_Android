package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.launch

class AddTrialViewModel : ViewModel() {
    private val trialRepositoryDummy = TrialRepositoryDummy()

    private val _testData : MutableLiveData<List<Trial>> = MutableLiveData(listOf())
    val testData: LiveData<List<Trial>> get() = _testData

    private val _navFrag = MutableLiveData<Boolean?>()
    val navFrag: LiveData<Boolean?> get() = _navFrag

    private val _collectState = MutableLiveData<String>()
    val collectState: LiveData<String> get() = _collectState

    fun navStart() {
        _navFrag.value = true
    }

    fun navCompleted() {
        _navFrag.value = null
    }

    init {
        getTrialList("testUserID")
    }

    fun getTrialList(userId: String) {
        viewModelScope.launch {
            trialRepositoryDummy.getTriedTrialByUserId(userId).collect{
                when(it) {
                    is Result.Loading -> _collectState.value = "Loading"
                    is Result.Success -> {
                        _collectState.value = "Success"
                        _testData.value = it.data!!}
                    is Result.Error -> _collectState.value = "Error"
                }
            }
        }
    }

//    fun getTrialList(userId: String) {
//        viewModelScope.launch {
//            trialRepositoryDummy.getTrialsNear(LatLng(26.526230, 128.030372), 100.0).collect{
//                when(it) {
//                    is Result.Loading -> _collectState.value = "Loading"
//                    is Result.Success -> {
//                        _collectState.value = "Success"
//                        _testData.value = it.data!!}
//                    is Result.Error -> _collectState.value = "Error"
//                }
//            }
//        }
//    }
}