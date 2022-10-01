package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialBinding

class AddTrialFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var binding: FragmentAddTrialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(AddTrialViewModel::class.java)
//        binding = FragmentAddTrialBinding.inflate(inflater, container, false)
        binding = FragmentAddTrialBinding.inflate(layoutInflater)

        viewModel.navFrag.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddTrialFragmentDirections.actionNavigationAddTrialToNavigationAddTrialOrigin())
                viewModel.navCompleted()
            }
        })

        binding.startButton.setOnClickListener { viewModel.navStart() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }
}