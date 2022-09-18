package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import java.util.*

class AddTrialMapsFragment : Fragment() {
    companion object {
        private const val ZOOM_SIZE = 14f
        private const val POLYLINE_WIDTH = 12f
    }

    private lateinit var map: GoogleMap
    private lateinit var polyline: Polyline
    private val overview = 0
    private lateinit var viewModel: AddTrialMapsViewModel

    private val callback = OnMapReadyCallback { googleMap ->
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
        viewModel = ViewModelProvider(this).get(AddTrialMapsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_trial_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.directionsResult.observe(viewLifecycleOwner, Observer{
            updatePolyline(it, map)
        })
    }

    //カメラを移動
    private fun moveCamera() {
        // Add a marker in Sydney and move the camera
        val origin = LatLng(viewModel.origin.value!!.lat, viewModel.origin.value!!.lng)
        map.apply {
            addMarker(MarkerOptions().position(origin).title("Marker in Origin"))
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

            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("drop")
                    .snippet(snippet)
                    .draggable(true)
            )

            Log.i("MapsActivity","doAddMarker")
            if (marker != null) {
                viewModel.addWaypointMarker(marker)
            }
            Log.i("MapsActivity","didAddMarker")
        }
    }

    //マーカーをクリック時にそのマーカーを削除
    private fun setMarkerClick(map: GoogleMap) {
        map.setOnMarkerClickListener{marker ->
            viewModel.removeWaypointMarker(marker)
            marker.remove()
            return@setOnMarkerClickListener true
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
                viewModel.changeWaypointMarker(marker)
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
        val decodedPath = PolyUtil.decode(directionsResult.routes[overview].overviewPolyline.encodedPath)
        polyline = map.addPolyline(polylineOptions.addAll(decodedPath))
    }
}