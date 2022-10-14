package jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto

import java.util.*

/**
 * トライアルの記録を表現するクラス
 *
 * @property userId この記録を保持している人のUserID
 * @property rank この記録の順位 データベースにまだ登録されていない記録には -1 が入る
 */
sealed class Record {
    abstract val userId: String
    abstract val trialId: String
    abstract val trialName: String
    abstract val date: String
    abstract val rank: Int
    abstract val recordId: String

    /**
     * マラソンのトライアルの記録を表現するクラス
     *
     * @property time 実際の記録 単位は秒
     */
    data class MarathonRecord(
        override val userId: String,
        override val trialId: String,
        override val trialName: String,
        override val date: String,
        val distance: Long,
        val time: Long,
        override val rank: Int = -1,
        override val recordId: String = UUID.randomUUID().toString(),
    ): Record()

    /**
     * 懸垂のトライアルの記録を表現するクラス
     *
     * @property time 実際の記録 単位は秒
     */
    data class DanglingRecord(
        override val userId: String,
        override val trialId: String,
        override val trialName: String,
        override val date: String,
        val time: Long,
        override val rank: Int = -1,
        override val recordId: String,
    ): Record()
}
