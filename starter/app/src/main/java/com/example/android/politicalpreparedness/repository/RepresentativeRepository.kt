package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RepresentativeRepository {
    /**
     * get Representatives from server
     * @param address The place address.
     * @return list of Representative object
     */
    suspend fun getRepresentatives(address: Address): List<Representative>
}

class RepresentativeRepositoryImpl(
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepresentativeRepository {

    override suspend fun getRepresentatives(address: Address): List<Representative> {
        return withContext(ioDispatcher) {
            try {
                val joinMap = mutableMapOf<Int, Representative>()
                val response = civicsApiService.getRepresentatives(address.toFormattedString())
                response.offices.forEachIndexed { index, office ->
                    joinMap[index] = Representative(office = office, official = Official(""))
                }
                response.officials.forEachIndexed { index, official ->
                    val current = joinMap[index]
                    val office = current?.office ?: Office("", Division(), listOf())
                    joinMap[index] = Representative(office = office, official = official)
                }
                joinMap.values.toList()
            } catch (e: Exception) {
                e.printStackTrace()
                listOf()
            }
        }
    }

}