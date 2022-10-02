package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.config_join_trial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentConfigJoinTrialBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.buildRequestBluetoothScanPermissionAlertDialog
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.checkBluetoothScanPermission
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.observeFlow

class ConfigJoinTrialFragment : Fragment() {

    companion object {
        private val TAG = ConfigJoinTrialFragment::class.simpleName
    }

    private lateinit var _binding: FragmentConfigJoinTrialBinding

    private val binding get() = _binding
    private val viewModel by viewModels<ConfigJoinTrialViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigJoinTrialBinding.inflate(inflater, container, false)

        val requestPermissionDialog =
            buildRequestBluetoothScanPermissionAlertDialog { isPermissionGranted ->
                if (!isPermissionGranted) {
                    Snackbar
                        .make(
                            binding.root,
                            R.string.feature_wear_os_is_disable,
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                    viewModel.saveIsUseWearOs(false)
                }
            }

        // FIXME スマートフォンのBluetoothがOFFだった時の処理を書くべき
        observeFlow(viewModel.selectedDeviceName) { deviceName ->
            if (deviceName == null) {
                binding.selectedDeviceName.text =
                    resources.getText(R.string.device_not_selected)
                binding.deviceConnectState.visibility = View.GONE
            } else {
                binding.selectedDeviceName.text = deviceName
                binding.deviceConnectState.visibility = View.VISIBLE
            }
        }

        observeFlow(viewModel.isUseWearOs) { isUseWearOs ->
            if (isUseWearOs && !checkBluetoothScanPermission()) {
                requestPermissionDialog.show()
            }
            binding.apply {
                selectedDeviceName.isEnabled = isUseWearOs
                deviceConnectState.isEnabled = isUseWearOs
                selectWearableDeviceTextButton.isEnabled = isUseWearOs
                isUseWearOsSwitch.isChecked = isUseWearOs
            }
        }
        binding.isUseWearOsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsUseWearOs(isChecked)
        }

        observeFlow(viewModel.isUseNrealAir) { isUseNrealAir ->
            binding.isUseNrealAirSwitch.isChecked = isUseNrealAir
        }
        binding.isUseNrealAirSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveIsUseNrealAir(isChecked)
        }

        binding.selectWearableDeviceTextButton.setOnClickListener {
            val action = ConfigJoinTrialFragmentDirections
                .actionNavigationConfigJoinTrialToNavigationSelectBleDevice()
            it.findNavController().navigate(action)
        }

        return binding.root
    }
}