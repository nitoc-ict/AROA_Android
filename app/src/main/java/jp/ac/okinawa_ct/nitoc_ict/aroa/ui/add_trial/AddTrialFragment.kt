package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialBinding

class AddTrialFragment : Fragment() {

    private var _binding: FragmentAddTrialBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(AddTrialViewModel::class.java)

        _binding = FragmentAddTrialBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAddTrial
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}