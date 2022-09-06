package jp.ac.okinawa_ct.nitoc_ict.aroa.data.repository

import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.flow.Flow

/**
 * [Trial] のリポジトリのインターフェース
 */
interface TrialRepository {

    /**
     * 指定されたIDを元に[Trial]を取得するメソッド
     *
     * @param id 取得する[Trial]のID
     */
    fun getTrialById(id: String): Flow<Result<Trial?>>

    /**
     * 指定されたユーザーが参加したことがある(記録が残っている)[Trial]のリストを取得するメソッド
     *
     * @param userId 検索したいユーザのUserID
     */
    fun getTriedTrialByUserId(userId: String): Flow<Result<List<Trial>>>

    /**
     * 指定された位置の周辺の[Trial]のリストを取得するメソッド
     *
     * @param location 中心となる位置
     * @param radius 中心からの半径.単位はメートル
     */
    fun getTrialsNear(location: LatLng, radius: Double): Flow<Result<List<Trial>>>

    /**
     * 引数で渡された[Trial]をデータベースに登録するメソッド
     *
     * @param trial
     */
    fun createTrial(trial: Trial): Flow<Result<Trial>>
}