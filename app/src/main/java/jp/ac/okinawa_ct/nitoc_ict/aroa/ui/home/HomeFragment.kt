package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var map : GoogleMap

    private lateinit var homeViewModel: HomeViewModel


    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        moveCamera()
        setMarkerClick(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        observeLiveData()
    }

    private fun observeLiveData() {
        homeViewModel.foundTrials.observe(viewLifecycleOwner) {
            for (data in it){
                map.addMarker(MarkerOptions().position(data.position).title(data.id))
            }
        }
    }

    private fun moveCamera() {
        val cUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(26.526230, 128.030372), 16f
        )
        map.apply {
            moveCamera(cUpdate)
        }
    }

    private fun setMarkerClick(map: GoogleMap) {
        homeViewModel.foundTrials.observe(viewLifecycleOwner) {
            map.setOnMarkerClickListener{marker ->
                marker.title?.let {
                        val action = HomeFragmentDirections.actionHomeFragmentToTrialDetailFragment(
                            it,true
                        )
                    this.findNavController().navigate(action)
                }
                return@setOnMarkerClickListener true
            }
        }
    }

}