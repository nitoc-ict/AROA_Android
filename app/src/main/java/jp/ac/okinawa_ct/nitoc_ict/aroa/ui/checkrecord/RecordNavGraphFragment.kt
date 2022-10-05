package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentCheckRecordListBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentCreateTrialBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentRecordNavGraphBinding

class RecordNavGraphFragment : Fragment() {
    private lateinit var binding: FragmentRecordNavGraphBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordNavGraphBinding.inflate(layoutInflater)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_check_record) as NavHostFragment
        val navController = navHostFragment.navController
        return binding.root
    }
}