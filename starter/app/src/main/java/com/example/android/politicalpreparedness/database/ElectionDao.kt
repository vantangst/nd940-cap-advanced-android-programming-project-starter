package com.example.android.politicalpreparedness.database


import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    // insert item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Insert
    fun insert(asteroid: Election)

    // select all election query
    @Query("select * from election_table")
    fun getAll(): Flow<List<Election>>

    @Query("select * from election_table")
    fun getAllWithoutFlow(): List<Election>

    @Query("select * from election_table where is_favorite = :isFavorite")
    fun getWith(isFavorite: Boolean): Flow<List<Election>>

    @Update
    fun update(election: Election): Int


    // select single election item
    @Query("select * from election_table where id = :electionId")
    fun getElection(electionId: Int): Election?

    //delete one item
    @Query("delete from election_table where id = :electionId")
    fun delete(electionId: Int): Int

    //clear all item
    @Query("delete from election_table")
    fun deleteAll(): Int

}