package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Result
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.map

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HoemFragment", "start")
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.foundTrials.observe(viewLifecycleOwner) {
            textView.text = it
        }
        Log.d("HoemFragment", "end")
        textView.setOnClickListener {
            Log.d("HoemFragment", "textViewClicked")
            homeViewModel.findTrialsNearMe()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}