package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.check_record.record_ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentRecordRankingBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.check_record.record_ranking.RecordRankingFragmentArgs

class RecordRankingFragment : Fragment() {
    private var _biding: FragmentRecordRankingBinding? = null
    private val binding get() = _biding!!
    private lateinit var viewModel: RecordRankingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentRecordRankingBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(RecordRankingViewModel::class.java)

        val args = RecordRankingFragmentArgs.fromBundle(requireArguments())
        viewModel.setTrialId(args.trialId)

        viewModel.trialId.observe(viewLifecycleOwner, Observer {
            viewModel.getRanking(it)
        })

        val adapter = RecordRankingListAdapter(requireContext())
        binding.rankingList.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(), LinearLayoutManager(requireContext()).getOrientation())
        binding.rankingList.addItemDecoration(dividerItemDecoration)

        viewModel.testRecordData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })


        return binding.root
    }

}