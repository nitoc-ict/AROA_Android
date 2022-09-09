package jp.ac.okinawa_ct.nitoc_ict.aroa.data.remote

import com.google.android.gms.maps.model.LatLng
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial

class TrialFierstoreDatasource: TrialRemoteDatasource {
    override suspend fun getTrialById(id: String): Result<Trial?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrialNear(location: LatLng, radius: Double): Result<List<Trial>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTriedTrialByUserId(userId: String): Result<List<Trial>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadTrial(trial: Trial): Response<Trial> {
        TODO("Not yet implemented")
    }

}