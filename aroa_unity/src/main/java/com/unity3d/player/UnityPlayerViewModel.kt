package com.unity3d.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.RecordRepositoryDummy
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepository
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class UnityPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val trialRepository: TrialRepository = TrialRepositoryDummy()
    private val recordRepository: RecordRepository = RecordRepositoryDummy()

    private val _trialName = MutableStateFlow("デモ用トライアル")
    val trialName: StateFlow<String> = _trialName

    private val _rank = MutableStateFlow(5)
    val rank: Flow<String> = _rank.map { rank ->
        return@map "${rank}位"
    }

    private val _time = MutableStateFlow(0)
    val time: Flow<String> = _time.map { time ->
        return@map "現在の記録${time / 60}分${time % 60}秒"
    }

    private val _rankState = MutableStateFlow(1)
    val rankState: Flow<String> = _rankState.map {
        it.toString()
    }

    private val _alertText = MutableStateFlow("")
    val alertText: Flow<String> = _alertText

    private var counterJob: Job? = null

    fun fetchTrialData(trialId: String) {
    }

    fun startTimer() {
        counterJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000)
                _time.emit(_time.value + 1)
            }
        }
    }

    fun stopTimer() {
        counterJob?.cancel()
    }

    fun startDemo() {
        viewModelScope.launch(Dispatchers.Default) {
            _alertText.emit("ここに説明が表示されます")
            delay(18000)
            showText(
                listOf(
                    "本来はこのテキストボックスは",
                    "トライアルの名前が表示されます",
                )
            )
            for (i in 1..3) {
                _alertText.emit("")
                delay(500)
                _alertText.emit("Gメッセ外周コース")
                delay(500)
            }
            delay(500)
            showText(
                listOf(
                    "では、早速デモを開始します",
                )
            )
            startTimer()
            showText(
                listOf(
                    "スタート!!",
                    "タイマーが起動しました",
                    "まずは斜め下をご覧ください",
                    "青の多角形がございます",
                    "これは前の記録との",
                    "距離を表しています",
                    "記録に近づくと......",
                )
            )
            for (rankState in 2..5) {
                _rankState.emit(rankState)
                delay(500)
            }
            showText(
                listOf(
                    "このように増えていきます",
                    "今は、青から赤の",
                    "計５個表示されています",
                    "この状態は",
                    "前の記録を追い越す寸前です",
                    "では、追い抜きましょう",
                )
            )
            rankUp()
            showText(
                listOf(
                    "順位が上がりました！",
                    "このようにして",
                    "他人の記録と競い合う事ができます。",
                    "以上で解説は終わりです。",
                    "ここからは擬似レースモードです",
                    "では、その場に立ったまま......",
                    "腿上げをしましょう!!",
                    "走っている自分をイメージしながら......",
                    "腿上げ開始!!",
                )
            )
            while (_rank.value > 1) {
                for (rankState in 1..5){
                    runBlocking {
                        _rankState.emit(rankState)
                    }
                    delay(1000)
                }
                rankUp()
            }
            _rankState.emit(0)
            showText(
                listOf(
                    "おめでとうございます!!",
                    "1位です!!",
                    "1位になると多角形は消えます",
                    "これはあなたが先頭だという証です!!",
                    "以上で擬似レースモードを終了します......",
                    "ご参加ありがとうございます"
                )
            )
        }
    }

    private fun rankUp() = runBlocking {
        if (_rank.value > 1) {
            _rank.emit(_rank.value - 1)
            _rankState.emit(1)
        }
    }

    private suspend fun showText(textList: List<String>) {
        textList.forEach { text ->
            _alertText.emit(text)
            delay(3000)
        }
    }
}