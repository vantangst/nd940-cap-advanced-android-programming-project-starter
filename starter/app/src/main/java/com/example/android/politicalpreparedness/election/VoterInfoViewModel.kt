package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: ElectionRepository) : ViewModel() {

    // Add live data to hold voter info
    private val _electionDataFlow = MutableStateFlow(Election())
    val electionDataFlow: StateFlow<Election>
        get() = _electionDataFlow

    //Add var and methods to populate voter info
    private val _administrationDataFlow = MutableStateFlow(AdministrationBody())
    val administrationDataFlow: StateFlow<AdministrationBody>
        get() = _administrationDataFlow

    fun initData(electionId: Int) {
        viewModelScope.launch {
            repository.getElection(electionId)?.let {
                _electionDataFlow.value = it
                getVoterInfo(it.division.address, it.id)
            }
        }
    }

    // Add var and methods to support loading URLs
    private fun getVoterInfo(address: String, electionId: Int) {
        viewModelScope.launch {
            repository.getVoterInfo(address, electionId)?.let {
                _administrationDataFlow.value = it
            }
        }
    }



    // Add var and methods to save and remove elections to local database
    fun toggleFavoriteElection() {
        val electionId = _electionDataFlow.value.id
        viewModelScope.launch {
            repository.getElection(electionId)?.let {
                repository.updateElection(it.copy(isFavorite = !it.isFavorite))
            }
            repository.getElection(electionId)?.let {
                _electionDataFlow.value = it
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}