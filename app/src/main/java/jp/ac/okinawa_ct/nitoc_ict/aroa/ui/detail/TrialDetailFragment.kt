package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentTrialDetailBinding

class TrialDetailFragment : Fragment() {
    private lateinit var _binding: FragmentTrialDetailBinding
    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->
        val okinawa = LatLng(26.526230, 128.030372)
        googleMap.addMarker(MarkerOptions().position(okinawa).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(okinawa))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrialDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}