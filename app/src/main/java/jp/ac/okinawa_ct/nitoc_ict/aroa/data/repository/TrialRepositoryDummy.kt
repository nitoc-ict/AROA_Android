package jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * テスト用のダミーのTrialを返す[TrialRepository]
 */
class TrialRepositoryDummy : TrialRepository {

    companion object {
        private val testDataList = mutableListOf<Trial>(
            Trial.Marathon(
                "沖縄高専外周コース1",
                "ひじかた",
                LatLng(26.526230, 128.030372),  // 沖縄高専
                listOf(
                    LatLng(26.526230, 128.030372),
                    LatLng(26.526227, 128.030645),
                    LatLng(26.526424, 128.030645),
                    LatLng(26.526424, 128.030372),
                    LatLng(26.526230, 128.030372),
                ), // 沖縄高専の敷地内を回るコース
                "2022月10月15日",
            ),
            Trial.Marathon(
                "沖縄高専外周コース2",
                "ひじかた",
                LatLng(26.526347, 128.029209),  // 沖縄高専
                listOf(
                    LatLng(26.526347, 128.029209),
                    LatLng(26.526230, 128.030372),
                    LatLng(26.526227, 128.030645),
                    LatLng(26.526424, 128.030645),
                    LatLng(26.526424, 128.030372),
                    LatLng(26.526347, 128.029209),
                ), // 沖縄高専の敷地内を回るコース
                "2022月10月15日",
            ),
            Trial.Marathon(
                "沖縄高専外周コース3",
                "ひじかた",
                LatLng(26.525823, 128.031147),  // 沖縄高専
                listOf(
                    LatLng(26.525823, 128.031147),
                    LatLng(26.526230, 128.030372),
                    LatLng(26.526227, 128.030645),
                    LatLng(26.526424, 128.030645),
                    LatLng(26.526424, 128.030372),
                    LatLng(26.525823, 128.031147),
                ), // 沖縄高専の敷地内を回るコース
                "2022月10月15日",
            ),
        )
    }

    override fun getTrialById(id: String): Flow<Result<Trial?>> =
        flow<Result<Trial>> {
            delay(1000) // ネットワーク処理の遅延の際限の為、1000ms 待つ
            emit(Result.Success(testDataList[0]))
            Log.d("TrialRepositoryDummy", testDataList[0].toString())
        }.catch {
            // 本来はここでエラー処理をする
            when (it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
            Log.d("TrialRepositoryDummy", "Loading...")
        }.flowOn(Dispatchers.IO)

    override fun getTriedTrialByUserId(userId: String): Flow<Result<List<Trial>>> =
        flow<Result<List<Trial>>> {
            delay(1000) // ネットワーク処理の遅延の際限の為、1000ms 待つ
            emit(Result.Success(testDataList))
            Log.d("TrialRepositoryDummy", "AddTrialFragment:${testDataList}")
        }.catch {
            // 本来はここでエラー処理をする
            when (it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
            Log.d("TrialRepositoryDummy", "Loading...")
        }.flowOn(Dispatchers.IO)

    override fun getTrialsNear(location: LatLng, radius: Double): Flow<Result<List<Trial>>> {
        return getTriedTrialByUserId("")
    }

    override fun createTrial(trial: Trial): Flow<Result<Trial>> =
        flow<Result<Trial>> {
            testDataList.add(trial)
            delay(500)
            emit(Result.Success(trial))
            Log.d("TrialRepositoryDummy", "createTrial:${testDataList}")
        }.onStart {
            emit(Result.Loading)
            Log.d("TrialRepositoryDummy", "Updating...")
        }.flowOn(Dispatchers.IO)
}