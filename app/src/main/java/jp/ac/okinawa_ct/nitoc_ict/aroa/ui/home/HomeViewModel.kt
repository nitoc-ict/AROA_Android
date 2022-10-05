package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val trialRepository = TrialRepositoryDummy()

    private val _foundTrials: MutableLiveData<List<Trial>> = MutableLiveData(listOf())
    val foundTrials: LiveData<List<Trial>> = _foundTrials

    init {
        findTrialsNearMe()
    }

    fun findTrialsNearMe() {

        viewModelScope.launch {

            trialRepository
                .getTrialsNear(LatLng(26.526230, 128.030372), 100.0)
                .collect {
                    when (it) {
                        is Result.Loading -> _foundTrials.value = listOf()
                        is Result.Success -> {
                            _foundTrials.value = it.data!!
                        }

                        is Result.Error -> {
                            _foundTrials.value = listOf()
                        }
                    }
                }
        }
    }
}