package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.squareup.moshi.*

@Entity(tableName = "election_table")
data class Election(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "electionDay") val electionDay: String = "",
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division = Division()
)