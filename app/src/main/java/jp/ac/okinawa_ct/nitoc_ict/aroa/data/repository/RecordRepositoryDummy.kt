package jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository

import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class RecordRepositoryDummy: RecordRepository {
    companion object {
        private val trial1RecordList = mutableListOf<Record>(
            Record.MarathonRecord(
                "サイス", "testTrialID_1","沖縄高専外周コース1","2022年10月15日",487,140,2,"testRecordId1"
            ),
            Record.MarathonRecord(
                "知念","testTrialID_1","沖縄高専外周コース1","2022年10月14日",487,150,3,"testRecordId2"
            ),
            Record.MarathonRecord(
                "ひじかた","testTrialID_1","沖縄高専外周コース1","2022年10月13日",487,110,1,"testRecordId3"
            ),
        )
        private val trial2RecordList = mutableListOf<Record>(
            Record.MarathonRecord(
                "サイス", "testTrialID_2","沖縄高専外周コース2","2022年10月15日",975,310,2,"testRecordId1"
            ),
            Record.MarathonRecord(
                "知念","testTrialID_2","沖縄高専外周コース2","2022年10月14日",975,340,3,"testRecordId2"
            ),
            Record.MarathonRecord(
                "ひじかた","testTrialID_2","沖縄高専外周コース2","2022年10月13日",975,250,1,"testRecordId3"
            ),
        )
        private val trial3RecordList = mutableListOf<Record>(
            Record.MarathonRecord(
                "サイス", "testTrialID_3","沖縄高専外周コース3","2022年10月15日",616,210,2,"testRecordId1"
            ),
            Record.MarathonRecord(
                "知念","testTrialID_3","沖縄高専外周コース3","2022年10月14日",616,290,3,"testRecordId2"
            ),
            Record.MarathonRecord(
                "ひじかた","testTrialID_3","沖縄高専外周コース3","2022年10月13日",616,180,1,"testRecordId3"
            ),
        )

        private val myRecordList = mutableListOf<Record>(
            Record.MarathonRecord(
                "ひじかた","testTrialID_1","沖縄高専外周コース1","2022年10月13日",487,110,1,"testRecordId1"
            ),
            Record.MarathonRecord(
                "ひじかた","testTrialID_2","沖縄高専外周コース2","2022年10月13日",975,250,1,"testRecordId2"
            ),
            Record.MarathonRecord(
                "ひじかた","testTrialID_3","沖縄高専外周コース3","2022年10月13日",616,180,1,"testRecordId3"
            ),
        )
    }


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
            when(trialId) {
                trial1RecordList.get(0).trialId -> emit(Result.Success(trial1RecordList.sortedWith(comparator)))
                trial2RecordList.get(0).trialId -> emit(Result.Success(trial2RecordList.sortedWith(comparator)))
                trial3RecordList.get(0).trialId -> emit(Result.Success(trial3RecordList.sortedWith(comparator)))
            }
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
            emit(Result.Success(myRecordList))
        }.catch {
            when(it) {
                is Exception -> {
                    emit(Result.Error(true, it))
                }
            }
        }.onStart {
            emit(Result.Loading)
        }.flowOn(Dispatchers.IO)

    override fun getRecordByTrialIdAndRecordId(trialId: String, recordId: String): Flow<Result<Record>> =
        flow<Result<Record>> {
            delay(1000)
            when(trialId) {
                trial1RecordList.get(0).trialId -> {
                    for (data in trial1RecordList) {
                        if (data.recordId == recordId) {
                            emit(Result.Success(data))
                        }
                    }
                }
                trial2RecordList.get(0).trialId -> {
                    for (data in trial2RecordList) {
                        if (data.recordId == recordId) {
                            emit(Result.Success(data))
                        }
                    }
                }
                trial3RecordList.get(0).trialId -> {
                    for (data in trial3RecordList) {
                        if (data.recordId == recordId) {
                            emit(Result.Success(data))
                        }
                    }
                }
            }
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

