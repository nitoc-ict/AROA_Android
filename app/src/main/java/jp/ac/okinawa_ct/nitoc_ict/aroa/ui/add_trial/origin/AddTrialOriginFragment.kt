package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.origin

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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialOriginBinding
import java.util.*

class AddTrialOriginFragment : Fragment() {

    companion object {
        private const val ZOOM_SIZE = 14f
    }

    private var map: GoogleMap? = null
    private lateinit var viewModel: AddTrialOriginViewModel

    private lateinit var _binding: FragmentAddTrialOriginBinding

    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
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
        _binding = FragmentAddTrialOriginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(AddTrialOriginViewModel::class.java)
        binding.nextButton.setOnClickListener { viewModel.navStart() }
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

    //カメラを移動
    private fun moveCamera() {
        val okinawa = LatLng(26.39987724386553, 127.74766655445417)
        map?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(okinawa, ZOOM_SIZE))
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
            //マップをクリア
            map.clear()

            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("drop")
                    .snippet(snippet)
                    .draggable(true)
            )

            if (marker != null) {
                viewModel.setOrigin(marker.position)
            }
        }
    }

    //マーカーをクリック時にそのマーカーを削除
    private fun setMarkerClick(map: GoogleMap) {
        map.setOnMarkerClickListener{marker ->
            viewModel.removeOrigin()
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
                viewModel.setOrigin(marker.position)
            }
        })
    }
}