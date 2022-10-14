package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial.dest

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialDestBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.ConverterVectorToBitmap
import java.util.*

class AddTrialDestFragment : Fragment() {

    companion object {
        private const val ZOOM_SIZE = 14f
        private const val POLYLINE_WIDTH = 12f
    }

    private var map: GoogleMap? = null
    private lateinit var viewModel: AddTrialDestViewModel
    private var polyline: Polyline? = null

    private lateinit var _binding: FragmentAddTrialDestBinding

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
        _binding = FragmentAddTrialDestBinding.inflate(inflater, container, false)
        val args = AddTrialDestFragmentArgs.fromBundle(
            requireArguments()
        )
        viewModel = ViewModelProvider(this).get(AddTrialDestViewModel::class.java)
        viewModel.setOrigin(args.originLatLng)
        binding.nextButton.setOnClickListener {
            if (viewModel.dest.value != null) {
                viewModel.navStart()
            }
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("トライアルを作成").setMessage("ゴール地点を設定")
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
            Log.i("DestFragment", "directionsResult:${it.toString()}")
            updatePolyline(it, map)
        })

        viewModel.navFrag.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                val action =
                    AddTrialDestFragmentDirections.actionNavigationAddTrialDestToNavigationAddTrialMaps(
                        LatLng(
                            viewModel.origin.value!!.latitude,
                            viewModel.origin.value!!.longitude
                        ),
                        LatLng(viewModel.dest.value!!.latitude, viewModel.dest.value!!.longitude)
                    )
                this.findNavController().navigate(action)
                viewModel.navCompleted()
            }
        })
    }

    //カメラを移動
    private fun moveCamera() {
        // Add a marker in Sydney and move the camera
        val origin = LatLng(viewModel.origin.value!!.latitude, viewModel.origin.value!!.longitude)
        map?.apply {
            addMarker(MarkerOptions()
                .position(origin)
                .title("Marker in Origin")
                .icon(BitmapDescriptorFactory.fromBitmap(
                    ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                requireContext(),R.drawable.ic_baseline_flag_circle_origin_36)))
                .anchor(0.5F,0.5F))
            moveCamera(CameraUpdateFactory.newLatLngZoom(origin, ZOOM_SIZE))
        }
    }

    //マップをロングクリック時にマーカーを追加
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener{latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            //マップをクリア、originのマーカーを追加
            map.clear()

            map.addMarker(
                MarkerOptions()
                    .position(
                    LatLng(
                        viewModel.origin.value!!.latitude,
                        viewModel.origin.value!!.longitude))
                    .title("Marker in Origin")
                    .icon(BitmapDescriptorFactory.fromBitmap(
                        ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                            requireContext(),R.drawable.ic_baseline_flag_circle_origin_36)))
                    .anchor(0.5F,0.5F))

            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marker in Dest")
                    .snippet(snippet)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                        ConverterVectorToBitmap().getBitmapFromVectorDrawable(
                            requireContext(),R.drawable.ic_baseline_flag_circle_dest_36)))
                    .anchor(0.5F,0.5F)
            )

            if (marker != null) {
                viewModel.setDest(marker.position)
            }
        }
    }


    //マーカーをドラッグ時に、マーカーのLatLngを更新
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
                viewModel.setDest(marker.position)
            }
        })
    }

    //Polylineを更新
    private fun updatePolyline(directionsResult: DirectionsResult?, googleMap: GoogleMap?) {
        googleMap ?: return
        directionsResult ?: return
        removePolyline()
        addPolyline(directionsResult, googleMap)
    }

    // 線を消す.
    private fun removePolyline() {
        if (map != null && polyline != null) {
            polyline?.remove()
        }
    }

    // 線を引く
    private fun addPolyline(directionsResult: DirectionsResult, map: GoogleMap) {
        val polylineOptions = PolylineOptions()
        polylineOptions.width(POLYLINE_WIDTH)
        // ARGB32bit形式.
        polylineOptions.color(R.color.map_polyline_stroke)
        val decodedPath = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.encodedPath)
        polyline = map.addPolyline(polylineOptions.addAll(decodedPath))
    }
}