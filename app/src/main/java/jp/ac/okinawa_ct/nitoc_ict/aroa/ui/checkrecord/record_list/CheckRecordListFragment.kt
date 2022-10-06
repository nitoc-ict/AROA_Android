package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentCheckRecordListBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.RecordListAdapter

class CheckRecordListFragment : Fragment() {

    private var _binding: FragmentCheckRecordListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: CheckRecordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(CheckRecordListViewModel::class.java)

        _binding = FragmentCheckRecordListBinding.inflate(inflater, container, false)

        val adapter = RecordListAdapter(requireContext())
        binding.recordList.adapter = adapter
        viewModel.testRecordList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        adapter.setOnItemClickListener { view, position ->
            val action = CheckRecordListFragmentDirections
                .actionCheckRecordListFragmentToRecordDetailFragment(
                    viewModel.testRecordList.value!!.get(position).recordId,
                    viewModel.testRecordList.value!!.get(position).trialId
                )
            this.findNavController().navigate(action)
        }

        return binding.root
    }
}