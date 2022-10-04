package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.start

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.FragmentAddTrialBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial.CreatedTrialAdapter

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




        val adapter = CreatedTrialAdapter(binding.createdTrialList.context)
        binding.createdTrialList.adapter = adapter
        adapter.setOnItemClickListener { view, position ->
            val action = AddTrialFragmentDirections.actionNavigationCreateTrialToTrialDetailFragment(
                viewModel.testData.value!!.get(position).id
            )
            this.findNavController().navigate(action)
            Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
        }

        viewModel.testData.observe(viewLifecycleOwner, Observer{
            it?.let {
                Log.i("AddTrialFragment", "${it}")
                adapter.submitList(it)
            }
        })

        viewModel.collectState.observe(viewLifecycleOwner, Observer {
            when(it) {
                "Loading" -> Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                "Success" -> Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                "Error" -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.navFrag.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                   AddTrialFragmentDirections.actionNavigationAddTrialToNavigationAddTrialOrigin()
                )
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