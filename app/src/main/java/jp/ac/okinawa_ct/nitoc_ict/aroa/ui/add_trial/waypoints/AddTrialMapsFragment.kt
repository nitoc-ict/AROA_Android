package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.waypoints

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialMapsBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.ConverterVectorToBitmap
import java.util.*

class AddTrialMapsFragment : Fragment() {
    companion object {
        private const val ZOOM_SIZE = 14f
        private const val POLYLINE_WIDTH = 12f
    }

    private var map: GoogleMap? = null
    private var polyline: Polyline? = null
    private val overview = 0
    private lateinit var viewModel: AddTrialMapsViewModel

    private lateinit var _binding: FragmentAddTrialMapsBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        viewModel.directionApiExecute()
        moveCamera()
        setMapLongClick(googleMap)
        setMarkerClick(googleMap)
        setOnMarkerDrag(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTrialMapsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AddTrialMapsViewModel::class.java)
        val args = AddTrialMapsFragmentArgs.fromBundle(
            requireArguments()
        )
        viewModel.setOrigin(args.originLatLng)
        viewModel.setDest(args.destLatLng)
        binding.saveButton.setOnClickListener { viewModel.createNewTrial() }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("????????????????????????").setMessage("?????????????????????")
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
        viewModel.directionsResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            updatePolyline(it, map)
        })

        viewModel.navFrag.observe(viewLifecycleOwner, Observer {
            if (it) {
                val action =
                    AddTrialMapsFragmentDirections.actionNavigationAddTrialMapsToNavigationAddTrial()
                this.findNavController().navigate(action)
                viewModel.navCompleted()
            }
        })
    }

    //??????????????????
    private fun moveCamera() {
        // Add a marker in Sydney and move the camera
        val origin = LatLng(viewModel.origin.value!!.latitude, viewModel.origin.value!!.longitude)
        val dest = LatLng(viewModel.dest.value!!.latitude, viewModel.dest.value!!.longitude)
        map?.apply {
            addMarker(MarkerOptions().position(origin).title("Marker in Origin")
                .icon(BitmapDescriptorFactory.fromBitmap(
                ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                    requireContext(),R.drawable.ic_baseline_flag_circle_origin_36)))
                .anchor(0.5F,0.5F))
            addMarker(MarkerOptions().position(dest).title("Marker in Dest")
                .icon(BitmapDescriptorFactory.fromBitmap(
                    ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                        requireContext(),R.drawable.ic_baseline_flag_circle_dest_36)))
                .anchor(0.5F,0.5F))
            moveCamera(CameraUpdateFactory.newLatLngZoom(origin, ZOOM_SIZE))
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

            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("drop")
                    .snippet(snippet)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                        requireContext(),R.drawable.ic_baseline_waypoints_circle_36)))
                    .anchor(0.5F,0.5F)
            )


            Log.i("MapsActivity", "doAddMarker")
            if (marker != null) {
                viewModel.addWaypointMarker(marker)
            }
            Log.i("MapsActivity", "didAddMarker")
        }
    }

    //????????????????????????????????????????????????????????????
    private fun setMarkerClick(map: GoogleMap) {
        map.setOnMarkerClickListener{marker ->
            if(marker.title != "Marker in Origin") {
                if (marker.title != "Marker in Dest") {
                    viewModel.removeWaypointMarker(marker)
                    marker.remove()
                }
            }
            return@setOnMarkerClickListener true
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
                viewModel.changeWaypointMarker(marker)
            }
        })
    }

    //Polyline?????????
    private fun updatePolyline(directionsResult: DirectionsResult?, googleMap: GoogleMap?) {
        googleMap ?: return
        directionsResult ?: return
        removePolyline()
        addPolyline(directionsResult, googleMap)
    }

    // ????????????.
    private fun removePolyline() {
        if (map != null && polyline != null) {
            polyline?.remove()
        }
    }

    // ????????????
    private fun addPolyline(directionsResult: DirectionsResult, map: GoogleMap) {
        val polylineOptions = PolylineOptions()
        polylineOptions.width(POLYLINE_WIDTH)
        // ARGB32bit??????.
        polylineOptions.color(R.color.map_polyline_stroke)
        val decodedPath =
            PolyUtil.decode(directionsResult.routes[overview].overviewPolyline.encodedPath)
        polyline = map.addPolyline(polylineOptions.addAll(decodedPath))
    }
}