package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ElectionsViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

    private val _favoriteElectionDataFlow = MutableStateFlow(false)
    val favoriteElectionDataFlow: StateFlow<Boolean>
        get() = _favoriteElectionDataFlow

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

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}