package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentTrialDetailBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.waypoints.AddTrialMapsFragment
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.waypoints.AddTrialMapsFragmentDirections
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.waypoints.AddTrialMapsViewModel

class TrialDetailFragment : Fragment() {
    companion object {
        private const val ZOOM_SIZE = 16f
        private const val POLYLINE_WIDTH = 12f
    }

    private var map: GoogleMap? = null
    private var polyline: Polyline? = null
    private lateinit var viewModel: TrialDetailViewModel

    private lateinit var _binding: FragmentTrialDetailBinding
    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrialDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(TrialDetailViewModel::class.java)
        val args = TrialDetailFragmentArgs.fromBundle(requireArguments())
        if (!args.canStartTrial) {
            binding.startTrialButton.visibility = View.INVISIBLE
        }else {
            binding.startTrialButton.setOnClickListener {

            }
        }
        viewModel.setTrialId(args.trialId)

        return binding.root
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

        viewModel.trialId.observe(viewLifecycleOwner, Observer {
            viewModel.getTrialById()
        })

        viewModel.trial.observe(viewLifecycleOwner, Observer {
            viewModel.assignmentTrialData()
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(viewModel.trialPosition.value!!, ZOOM_SIZE))
            binding.detailTrialName.text = viewModel.trialName.value
            binding.detailTrialAuthorUserId.text = viewModel.trialId.value
            binding.detailTrialPosition.text = viewModel.trialPosition.value.toString()
        })

        viewModel.trialCourse.observe(viewLifecycleOwner, Observer {
            viewModel.courseTranslate()
            viewModel.directionApiExecute()
            map?.addMarker(MarkerOptions().position(viewModel.origin.value!!).title("origin"))
            map?.addMarker(MarkerOptions().position(viewModel.dest.value!!).title("dest"))
        })
        viewModel
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
        val decodedPath =
            PolyUtil.decode(directionsResult.routes[0].overviewPolyline.encodedPath)
        polyline = map.addPolyline(polylineOptions.addAll(decodedPath))
    }
}