package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: RepresentativeRepository) : ViewModel() {

    //Create function to fetch representatives from API from a provided address
    private val _representativesDataFlow = MutableStateFlow<List<Representative>>(listOf())
    val representativesDataFlow: StateFlow<List<Representative>>
        get() = _representativesDataFlow

    //Establish live data for representatives and address
    private val _addressDataFlow = MutableStateFlow(
        Address(
            "",
            city = "",
            state = "",
            zip = ""
        )
    )
    val addressDataFlow: StateFlow<Address>
        get() = _addressDataFlow

    fun setAddressState(state: String) {
        viewModelScope.launch {
            _addressDataFlow.value = _addressDataFlow.value.copy(state = state)
        }
    }

    fun setAddress(address: Address) {
        viewModelScope.launch {
            _addressDataFlow.value = address
        }
    }

    fun getRepresentatives() {
        Log.i("RepresentativeViewModel", "address Selected: ${addressDataFlow.value}")
        viewModelScope.launch {
            val result =
                repository.getRepresentatives(addressDataFlow.value)
            _representativesDataFlow.value = result
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

}
