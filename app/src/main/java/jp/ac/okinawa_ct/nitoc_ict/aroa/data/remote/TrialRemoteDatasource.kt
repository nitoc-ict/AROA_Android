package jp.ac.okinawa_ct.nitoc_ict.aroa.data.remote

import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial

/**
 * Trialの情報を取得するリモートデータソース
 */
interface TrialRemoteDatasource {
    /**
     * 指定されたIDを元に[Trial]を取得するメソッド
     * 存在しない場合はnullを返す
     *
     * @param id 取得する[Trial]のID
     */
    suspend fun getTrialById(id: String): Response<Trial?>

    /**
     * 指定されたユーザーが参加したことがある(記録が残っている)[Trial]のリストを取得するメソッド
     *
     * @param userId 検索したいユーザのUserID
     */
    suspend fun getTriedTrialByUserId(userId: String): Response<List<Trial>>

    /**
     * 指定された位置の周辺の[Trial]のリストを取得するメソッド
     *
     * @param location 中心となる位置
     * @param radius 中心からの半径.単位はメートル
     */
    suspend fun getTrialNear(location: LatLng, radius: Double): Response<List<Trial>>

    /**
     * 引数で渡された[Trial]をデータベースに登録するメソッド
     *
     * @param trial
     */
    suspend fun uploadTrial(trial: Trial): Response<Trial>
}