package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.create_trial

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentCreateTrialBinding

class CreateTrialFragment : Fragment() {

    companion object {
        fun newInstance() = CreateTrialFragment()
    }

    private lateinit var viewModel: CreateTrialViewModel
    private lateinit var binding: FragmentCreateTrialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTrialBinding.inflate(layoutInflater)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_create_trial) as NavHostFragment
        val navController = navHostFragment.navController
        return binding.root
    }
}