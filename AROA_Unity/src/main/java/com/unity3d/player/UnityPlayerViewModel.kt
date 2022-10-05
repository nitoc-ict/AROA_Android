package com.unity3d.player

import android.app.Application
import android.content.res.Resources.NotFoundException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.local.UserPreferences
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UnityPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val trialRepository: TrialRepository = TrialRepositoryDummy()
    private val recordRepository: RecordRepository = RecordRepositoryDummy()

    private val trial = MutableStateFlow<Result<Trial.Marathon>>(Result.Loading)

    val trialName: Flow<Result<String>> = trial.map { result ->
        when (result) {
            is Result.Loading -> result
            is Result.Error -> result
            is Result.Success -> Result.Success<String>(result.data.name)
        }
    }

    private val _recordTime = MutableStateFlow(0)
    val recordTime: StateFlow<Int> = _recordTime

    private val _relativeDistance = MutableStateFlow(0)
    val relativeDistance: StateFlow<Int> = _relativeDistance

    private val _currentRank = MutableStateFlow(0)
    val currentRank: StateFlow<Int> = _currentRank

    fun fetchTrialById(trialId: String) {
        viewModelScope.launch {
            trialRepository.getTrialById(trialId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        when (result.data) {
                            null -> {
                                trial.emit(
                                    Result.Error(
                                        false,
                                        NotFoundException("指定されたIDのトライアルは存在しません。")
                                    )
                                )
                            }
                            is Trial.Marathon -> {
                                trial.emit(
                                    Result.Error(
                                        false,
                                        java.lang.IllegalArgumentException("MarathonトライアルのIDではありません。")
                                    )
                                )
                            }
                            else -> {
                                trial.emit(result as Result<Trial.Marathon>)
                            }
                        }
                    }
                    is Result.Loading -> trial.emit(result)
                    is Result.Error -> trial.emit(result)
                }
            }
        }
    }

    fun startCount() {
        viewModelScope.launch {
            for (i in 0..500) {
                _recordTime.emit(i)
                delay(1000)
            }
        }
    }
}