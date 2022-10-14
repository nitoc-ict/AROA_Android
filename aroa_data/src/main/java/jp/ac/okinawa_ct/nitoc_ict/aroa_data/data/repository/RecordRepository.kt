package jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository

import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.flow.Flow

/**
 * [Record] のリポジトリのインターフェース
 */
interface RecordRepository {

    /**
     * [Trial]からuserIDを元に[Record]を探すメソッド
     *
     * @param trial [Record]を探す元の[Trial]
     * @param userId 目標のUserID
     */
    fun getRecordByUserId(trial: Trial, userId: String): Flow<Result<Record?>>

    /**
     * [Trial]のランキングから、startRankで指定された順位より下位の[Record]のリストを取得するメソッド
     * 例: getRecords(trial, 100, 10) この場合は100位から109位まで取得できる
     *
     * @param trial [Record]を探す元の[Trial]
     * @param startRank 何位から取得するか. 1以上である必要がある。
     * @param maxResult startRankで指定された順位から最大でいくつの[Record]を取得するか
     */
    fun getRecords(trialId: String, startRank: Int, maxResult: Int): Flow<Result<List<Record>>>

    /**
     * 指定された[Trial]のランキングに[Record]を追加する.すでに同じユーザーの記録がある場合は上書きする
     *
     * @param trial 記録を追加する[Trial]
     * @param record 実際に追加する[Record]
     *
     * @return データベースへの送信委成功した時は実際のランキングが保存された[Record]
     */
    fun createOrUpdateRecord(trial: Trial, record: Record): Flow<Result<Record>>

    fun getMyRecords(userId: String): Flow<Result<List<Record>>>

    fun getRecordByRecordId(recordId: String): Flow<Result<Record>>
}