package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

interface ElectionRepository {

    /**
     * Get Elections from server and store to local database
     */
    suspend fun refresh()

    /**
     * Get Elections from local database
     * @return list of Election
     */
    suspend fun getElections(): Flow<List<Election>>

    /**
     * Get Elections with filter from local database
     * @param isFavorite is a boolean value; default is true
     * @return list of Election
     */
    suspend fun getElectionsWith(isFavorite: Boolean = true): Flow<List<Election>>

    /**
     * Update an Elections to local database
     * @param election an Election object
     * @return true if success
     */
    suspend fun updateElection(election: Election): Boolean

    /**
     * Delete Elections from local database
     * @param electionId the id of election
     * @return true if success
     */
    suspend fun deleteElection(electionId: Int): Boolean

    /**
     * get Elections from local database
     * @param electionId the id of election
     * @return an Election if success, null if failed
     */
    suspend fun getElection(electionId: Int): Election?
}

class ElectionRepositoryImpl(
    private val electionDao: ElectionDao,
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionRepository {

    override suspend fun getElections(): Flow<List<Election>> {
        return electionDao.getAll()
    }

    override suspend fun getElectionsWith(isFavorite: Boolean): Flow<List<Election>> {
        return electionDao.getWith(isFavorite)
    }

    override suspend fun refresh() {
        coroutineScope {
            launch(ioDispatcher) {
                try {
                    val joinMap = mutableMapOf<Int, Election>()
                    civicsApiService.getElections().elections.forEach {
                        joinMap[it.id] = it
                    }

                    electionDao.getAllWithoutFlow().forEach { currentElection ->
                        val existedElection = joinMap[currentElection.id]
                        if (existedElection != null) {
                            joinMap[currentElection.id] =
                                existedElection.copy(
                                    isFavorite = currentElection.isFavorite
                                )
                        } else {
                            joinMap[currentElection.id] = currentElection
                        }
                    }
                    electionDao.insertAll(*joinMap.values.toTypedArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override suspend fun updateElection(election: Election): Boolean {
        return withContext(ioDispatcher) {
            try {
                electionDao.update(election) > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun deleteElection(electionId: Int): Boolean {
        return withContext(ioDispatcher) {
            try {
                electionDao.delete(electionId) > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun getElection(electionId: Int): Election? {
        return withContext(ioDispatcher) {
            electionDao.getElection(electionId)
        }
    }

}