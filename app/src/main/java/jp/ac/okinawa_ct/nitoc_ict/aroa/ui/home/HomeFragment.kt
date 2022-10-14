package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentHomeBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.ConverterVectorToBitmap


class HomeFragment : Fragment() {
    private val REQUEST_LOCATION_PERMISSION = 1

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
        enableMyLocation()
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
                map.addMarker(MarkerOptions()
                    .position(data.position)
                    .title(data.id)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                        requireContext(),R.drawable.ic_baseline_trial_circle_36)))
                    .anchor(0.5F,0.5F))
            }
        }
    }

    private fun moveCamera() {
        val cUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(26.526230, 128.030372), 14f
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

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

}