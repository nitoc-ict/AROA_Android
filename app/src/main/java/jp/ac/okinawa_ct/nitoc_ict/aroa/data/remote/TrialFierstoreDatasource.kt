package jp.ac.okinawa_ct.nitoc_ict.aroa.data.remote

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import kotlinx.coroutines.tasks.await

class TrialFierstoreDatasource: TrialRemoteDatasource {

    override suspend fun getTrialById(id: String): Response<Trial?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrialNear(location: LatLng, radius: Double): Response<List<Trial>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTriedTrialByUserId(userId: String): Response<List<Trial>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadTrial(trial: Trial): Response<Trial> {
        TODO("Not yet implemented")
    }

}