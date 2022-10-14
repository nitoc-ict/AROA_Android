package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_detail

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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentRecordDetailBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_detail.RecordDetailFragmentArgs
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.TimeFormat

class RecordDetailFragment : Fragment() {

    companion object {
        private const val ZOOM_SIZE = 16f
        private const val POLYLINE_WIDTH = 12f
    }

    private var _binding: FragmentRecordDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecordDetailViewModel

    private var map: GoogleMap? = null
    private var polyline: Polyline? = null

    private lateinit var args: RecordDetailFragmentArgs

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(RecordDetailViewModel::class.java)
        args = RecordDetailFragmentArgs.fromBundle(requireArguments())

        viewModel.setTrialId(args.trialId)
        viewModel.getTrialById()
        viewModel.setRecordId(args.recordId)
        viewModel.getRecordById()

        binding.checkRankingButton.setOnClickListener {
            viewModel.navStart()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.record.observe(viewLifecycleOwner, Observer {
            viewModel.assignmentRecordDate()
            binding.recordTime.text = TimeFormat().convertLongToTimeString(viewModel.recordTime.value!!)

        })

        viewModel.trial.observe(viewLifecycleOwner, Observer {
            viewModel.assignmentTrialData()
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom((viewModel.trial.value!!.position), ZOOM_SIZE)
            )
        })

        viewModel.trialCourse.observe(viewLifecycleOwner, Observer {
            viewModel.courseTranslate()
            viewModel.directionApiExecute()
            map?.addMarker(MarkerOptions().position(viewModel.origin.value!!).title("origin"))
            map?.addMarker(MarkerOptions().position(viewModel.dest.value!!).title("dest"))
        })

        viewModel.directionsResult.observe(viewLifecycleOwner, Observer{
            updatePolyline(it, map)
            viewModel.getDistance()
            binding.recordDistance.text = viewModel.trialDistance.value.toString() + "m"
            val speed = viewModel.trialDistance.value!! / viewModel.recordTime.value!! * 3.6
            binding.recordSpeed.text = speed.toString() + "km/h"
        })

        viewModel.navFrag.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = RecordDetailFragmentDirections.actionRecordDetailFragmentToRecordRankingFragment(
                    args.trialId
                )
                this.findNavController().navigate(action)
                viewModel.navCompleted()
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
        val decodedPath =
            PolyUtil.decode(directionsResult.routes[0].overviewPolyline.encodedPath)
        polyline = map.addPolyline(polylineOptions.addAll(decodedPath))
    }
}