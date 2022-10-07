package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.select_ble_device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentSelectBleDeviceBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.observeFlow

/**
 * スマートフォンとペアリングされているBluetoothLE対応の機器の一覧を表示するFragment
 */
class SelectBleDeviceFragment : Fragment() {

    companion object {
        private val TAG = SelectBleDeviceViewModel::class.simpleName
    }

    private lateinit var _binding: FragmentSelectBleDeviceBinding
    private val binding get() = _binding

    private val viewModel by viewModels<SelectBleDeviceViewModel>()

    private lateinit var adapter: DeviceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBleDeviceBinding.inflate(inflater, container, false)
        adapter = DeviceListAdapter(requireContext())

        binding.deviceList.adapter = adapter
        binding.deviceList.layoutManager = LinearLayoutManager(requireContext())
        binding.deviceList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.fetchBoundedDeviceList()

        adapter.setOnItemClickListener { view, position ->
            viewModel.saveSelectedDeviceAddress(
                viewModel.boundedDeviceList.value[position].address
            )
            val action = SelectBleDeviceFragmentDirections
                .actionNavigationSelectBleDeviceToNavigationConfigJoinTrial()
            view.findNavController().navigate(action)
        }

        observeFlow(viewModel.boundedDeviceList) {
            if (it.isEmpty()) {
                binding.errorText.visibility = View.VISIBLE
            } else {
                binding.errorText.visibility = View.GONE
                adapter.submitList(it)
            }
        }

        return binding.root
    }
}