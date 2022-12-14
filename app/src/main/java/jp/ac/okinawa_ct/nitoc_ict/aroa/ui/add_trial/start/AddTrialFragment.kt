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

    private lateinit var binding: FragmentAddTrialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(AddTrialViewModel::class.java)
        binding = FragmentAddTrialBinding.inflate(layoutInflater)

        val adapter = CreatedTrialAdapter(binding.createdTrialList.context)
        binding.createdTrialList.adapter = adapter
        //RecyclerViewのclickListener
        adapter.setOnItemClickListener { view, position ->
            val action = AddTrialFragmentDirections.actionNavigationCreateTrialToTrialDetailFragment(
                viewModel.testData.value!!.get(position).id,false
            )
            this.findNavController().navigate(action)
        }

        viewModel.testData.observe(viewLifecycleOwner, Observer{
            it?.let {
                Log.i("AddTrialFragment", "${it}")
                adapter.submitList(it)
            }
        })

        //トライアルの作成開始
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
}