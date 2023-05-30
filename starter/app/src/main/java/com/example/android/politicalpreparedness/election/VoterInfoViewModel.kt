package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val repository: ElectionRepository) : ViewModel() {

    private val _electionDataFlow = MutableStateFlow(Election())
    val electionDataFlow: StateFlow<Election>
        get() = _electionDataFlow

    fun initData(electionId: Int) {
        viewModelScope.launch {
            repository.getElection(electionId)?.let {
                _electionDataFlow.value = it
            }
        }
    }



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

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}