package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ElectionsViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

    //Create live data val for upcoming elections
    suspend fun getUpcomingElectionsDataFlow(): StateFlow<List<Election>> {
        return electionRepository.getElections().stateIn(viewModelScope)
    }

    suspend fun getSavedElectionsDataFlow(): StateFlow<List<Election>> {
        return electionRepository.getElectionsWith(isFavorite = true).stateIn(viewModelScope)
    }

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            electionRepository.refresh()
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info

}