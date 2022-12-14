package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.origin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialOriginBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.ConverterVectorToBitmap
import java.util.*

class AddTrialOriginFragment : Fragment() {

    companion object {
        private const val ZOOM_SIZE = 14f
    }
    private val REQUEST_LOCATION_PERMISSION = 1

    private var map: GoogleMap? = null
    private lateinit var viewModel: AddTrialOriginViewModel

    private lateinit var _binding: FragmentAddTrialOriginBinding
    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        moveCamera()
        setMapLongClick(googleMap)
        setOnMarkerDrag(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTrialOriginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AddTrialOriginViewModel::class.java)
        binding.nextButton.setOnClickListener {
            if (viewModel.origin.value != null) {
                viewModel.navStart()
            }
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("????????????????????????").setMessage("???????????????????????????")
            .setPositiveButton("ok",null)
        builder.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.navFrag.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                Log.i("originValue", viewModel.origin.value.toString())
                val action =
                   AddTrialOriginFragmentDirections.actionNavigationAddTrialOriginToNavigationAddTrialDest(
                        LatLng(
                            viewModel.origin.value!!.latitude,
                            viewModel.origin.value!!.longitude
                        )
                    )
                this.findNavController().navigate(action)
                viewModel.navCompleted()
            }
        })
    }

    //??????????????????
    private fun moveCamera() {
        val okinawa = LatLng(26.39987724386553, 127.74766655445417)
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(okinawa, ZOOM_SIZE))
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map!!.isMyLocationEnabled = true
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

    //????????????????????????????????????????????????????????????
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener{latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            //?????????????????????
            map.clear()

            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marker in Dest")
                    .snippet(snippet)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                        requireContext(),R.drawable.ic_baseline_flag_circle_origin_36)))
                    .anchor(0.5F,0.5F)
            )

            if (marker != null) {
                viewModel.setOrigin(marker.position)
            }
        }
    }

    //???????????????????????????????????????????????????LatLng?????????
    private fun setOnMarkerDrag(map: GoogleMap) {
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            private var start: com.google.maps.model.LatLng? = null
            private var end: com.google.maps.model.LatLng? = null

            override fun onMarkerDragStart(marker: Marker) {
                marker.position.let { start =
                    com.google.maps.model.LatLng(it.latitude, it.longitude)
                }
            }

            override fun onMarkerDrag(marker: Marker) {
                // Do Nothing.
            }

            override fun onMarkerDragEnd(marker: Marker) {
                marker.position.let { end =
                    com.google.maps.model.LatLng(it.latitude, it.longitude)
                }
                viewModel.setOrigin(marker.position)
            }
        })
    }
}