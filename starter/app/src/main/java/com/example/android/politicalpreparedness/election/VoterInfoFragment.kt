package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : Fragment() {
    private val viewModel: VoterInfoViewModel by viewModel()
    private lateinit var binding: FragmentVoterInfoBinding
    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Add binding values
        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.initData(args.argElectionId)

        //Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        return binding.root

    }

}