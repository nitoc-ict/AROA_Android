package jp.ac.okinawa_ct.nitoc_ict.aroa.data.remote

import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial

interface RecordRemoteDatasource {

    /**
     * [Trial]からuserIDを元に[Record]を探すメソッド
     *
     * @param trialId [Record]を探す元の[Trial]のID
     * @param userId 目標のUserID
     */
    suspend fun getRecordByUserId(trialId: String, userId: String): Response<Record?>

    /**
     * [Trial]のランキングから、startRankで指定された順位より下位の[Record]のリストを取得するメソッド
     * 例: getRecords(trial, 100, 10) この場合は100位から109位まで取得できる
     *
     * @param trialId [Record]を探す元の[Trial]のID
     * @param startRank 何位から取得するか. 1以上である必要がある。
     * @param maxResult startRankで指定された順位から最大でいくつの[Record]を取得するか
     */
    suspend fun getRecords(trialId: String, startRank: Int, maxResult: Int): Response<List<Record>>

    /**
     * 指定された[Trial]のランキングに[Record]を追加する.すでに同じユーザーの記録がある場合は上書きする
     *
     * @param trialId 記録を追加する[Trial]のID
     * @param record 実際に追加する[Record]
     *
     * @return データベースへの送信委成功した時は実際のランキングが保存された[Record]
     */
    suspend fun createOrUpdateRecord(trialId: String, record: Record): Response<Record>
}