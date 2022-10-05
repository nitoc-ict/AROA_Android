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
            "testUserId1", 140,2
        ),
        Record.MarathonRecord(
            "testUserId2",150,3
        ),
        Record.MarathonRecord(
            "testUserId3",110,1
        ),
    )

    override fun getRecordByUserId(trial: Trial, userId: String): Flow<Result<Record?>> {
        TODO("Not yet implemented")
    }

    override fun getRecords(
        trial: Trial,
        startRank: Int,
        maxResult: Int
    ): Flow<Result<List<Record>>> {
        TODO("Not yet implemented")
    }

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
}

