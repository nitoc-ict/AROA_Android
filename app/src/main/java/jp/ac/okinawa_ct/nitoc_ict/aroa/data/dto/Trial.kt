package jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto

import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * トライアルを表現するクラス
 *
 * @property name このトライアルの表示名
 * @property authorUserId このトライアルの作成者のユーザーID
 * @property position このトライアルのスタート地点
 * @property id このトライアルを識別する為の一意のID
 */
sealed class Trial {
    abstract val name: String
    abstract val authorUserId: String
    abstract val position: LatLng
    abstract val createDate: String
    abstract val id: String

    /**
     * マラソンのトライアルの詳細を表現するクラス
     *
     * @property course コースの経路情報が[LatLng]の[List]で表現されている
     */
    data class Marathon(
        override val name: String,
        override val authorUserId: String,
        override val position: LatLng,
        val course: List<LatLng>,
        override val createDate: String,
        override val id: String = UUID.randomUUID().toString(),
    ) : Trial()

    /**
     * 懸垂のトライアルの詳細を表現するクラス
     *
     * @property goalCount 懸垂をする回数
     */
    data class Dangling(
        override val name: String,
        override val authorUserId: String,
        override val position: LatLng,
        val goalCount: Int,
        override val createDate: String,
        override val id: String = UUID.randomUUID().toString(),
    ) : Trial()
}
