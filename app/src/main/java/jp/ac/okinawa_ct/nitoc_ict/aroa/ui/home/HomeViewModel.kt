package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository.TrialRepositoryDummy
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val trialRepository = TrialRepositoryDummy()

    private val _foundTrials = MutableLiveData("InitialData")
    val foundTrials: LiveData<String> = _foundTrials

    fun findTrialsNearMe() {
        /**
         * TrialRepositoryのメソッドは、必ず viewModelScope の launch というメソッドで新しいコルーチンを作成してその中で呼び出す必要がある。
         * とりあえず、
         * viewModelScope.launch { この中でTrialRepositoryのメソッドを呼び出す }
         * この形だけ覚えておけばOK
         */
        viewModelScope.launch {

            /**
             * TrialRepositoryにはいくつかメソッドが用意されている。詳しくは、コメントに書いてあるので、定義先を見ると良い
             */
            trialRepository
                /**
                 * getTrialsNear() の返り値は、Flow<Result<List<Trial>>> である。
                 */
                .getTrialsNear(LatLng(26.526230, 128.030372), 100.0)
                /**
                 * collect() は、Flowに用意されているメソッドで、このメソッドにラムダ式を渡すと、
                 * Flowの中身が変更されたときにそのラムダ式を実行する
                 */
                .collect {
                    /**
                     * collectに渡すラムダ式は、変更された値をitという名前で渡してくれる
                     * この場合は、Result<List<Trial>> である。
                     */
                    when (it) {
                        /**
                         * [Result]は、sealed class であり、これ自体をインスタンス化することはできず、
                         * 必ず[Result]を継承した [Result.Loading] [Result.Success] [Result.Error] のインスタンスしか存在しない。
                         * is 演算子は型を判定するので、このように it の実際の型を判定し、それに応じて処理を行っている。
                         * ちなみに、 最初は it は[Result]型としか認識されていないため、型判定をしない状態だと
                         * 強制キャストしない限り [Result.Loading] や [Result.Success] [Result.Error] としては扱えない。
                         */
                        is Result.Loading -> _foundTrials.value = "Loading" /** ここでは it を[Result.Loading]として扱うことができる */
                        is Result.Success -> {
                            /**
                             * ここでは it を [Result.Success]として扱うことができる
                             * なので、取得に成功したデータを取り出して、加工して [_foundTrials] に代入している。
                             */
                            _foundTrials.value = it.data.toString()
                        }
                        is Result.Error -> {
                            /** ここでは it を [Result.Error]として扱うことができる */
                            _foundTrials.value = it.exception.toString()
                        }
                    }
                }
        }
    }
}