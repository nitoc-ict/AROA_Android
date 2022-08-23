package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentChechRecordBinding

class CheckRecordFragment : Fragment() {

    private var _binding: FragmentChechRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(CheckRecordViewModel::class.java)

        _binding = FragmentChechRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textChechRecord
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}