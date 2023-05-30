package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {
    private lateinit var binding: FragmentElectionBinding

    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Add binding values
        binding = FragmentElectionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ElectionListAdapter(ElectionListener {
            gotoVoterInfo(it)
        })
        binding.rvUpcoming.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUpcomingElectionsDataFlow().collect {
                    lifecycleScope.launch {
                        adapter.submitList(it)
                    }
                }
            }
        }

        val savedAdapter = ElectionListAdapter(ElectionListener {
            gotoVoterInfo(it)
        })
        binding.rvSaved.adapter = savedAdapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getSavedElectionsDataFlow().collect {
                    lifecycleScope.launch {
                        savedAdapter.submitList(it)
                    }
                }
            }
        }
        return binding.root
    }

    private fun gotoVoterInfo(election: Election) {
        val action = ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division)
        findNavController().navigate(action)
    }

}