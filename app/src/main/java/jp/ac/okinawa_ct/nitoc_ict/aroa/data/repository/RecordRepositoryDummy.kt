package jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository

import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class RecordRepositoryDummy: RecordRepository {
    private val testRecordList = mutableListOf<Record>(
        Record.MarathonRecord(
            "サイス", "testTrialID_1","沖縄高専外周","2022年10月15日",487,140,2,"testRecordId1"
        ),
        Record.MarathonRecord(
            "知念","testTrialID_1","沖縄高専外周","2022年10月14日",487,150,3,"testRecordId2"
        ),
        Record.MarathonRecord(
            "ひじかた","testTrialID_1","沖縄高専外周","2022年10月13日",487,110,1,"testRecordId3"
        ),
    )

    override fun getRecordByUserId(trial: Trial, userId: String): Flow<Result<Record?>> {
        TODO("Not yet implemented")
    }

    override fun getRecords(
        trialId: String,
        startRank: Int,
        maxResult: Int
    ): Flow<Result<List<Record>>> =
        flow<Result<List<Record>>> {
            delay(1000)
            val comparator : Comparator<Record> = compareBy { it.rank }
            emit(Result.Success(testRecordList.sortedWith(comparator)))
        }.catch {
            when(it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
        }.flowOn(Dispatchers.IO)

    override fun createOrUpdateRecord(trial: Trial, record: Record): Flow<Result<Record>> {
        TODO("Not yet implemented")
    }

    override fun getMyRecords(userId: String): Flow<Result<List<Record>>> =
        flow<Result<List<Record>>> {
            delay(1000)
            emit(Result.Success(testRecordList))
        }.catch {
            when(it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
        }.flowOn(Dispatchers.IO)

    override fun getRecordByRecordId(recordId: String): Flow<Result<Record>> =
        flow<Result<Record>> {
            delay(1000)
            emit(Result.Success(testRecordList[0]))
        }.catch {
            when(it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
        }.flowOn(Dispatchers.IO)
}

