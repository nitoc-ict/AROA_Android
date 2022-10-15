package com.unity3d.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UnityPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val trialRepository: TrialRepository = TrialRepositoryDummy()
    private val recordRepository: RecordRepository = RecordRepositoryDummy()

    private val _trialName = MutableStateFlow("")
    val trialName: StateFlow<String> = _trialName

    private val _rank = MutableStateFlow(1)
    val rank: Flow<String> = _rank.map { rank ->
        return@map "${rank}位"
    }

    private val _time = MutableStateFlow(0)
    val time: Flow<String> = _time.map { time ->
        return@map "現在の記録${time/60}分${time%60}秒"
    }

    val rankState: Flow<String> = _time.map {
        ((it * 3 % 5) + 1).toString()
    }

    private var counterJob: Job? = null

    fun fetchTrialData(trialId: String) {

    }
    fun startTimer() {
        counterJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _time.emit(_time.value + 1)
        }
    }

    fun stopTimer() {
        counterJob?.cancel()
    }
}