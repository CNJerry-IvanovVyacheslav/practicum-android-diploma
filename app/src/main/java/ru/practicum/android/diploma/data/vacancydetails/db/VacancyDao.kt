package ru.practicum.android.diploma.data.vacancydetails.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVacancyToFavourite(vacancyId: DetailVacancyEntity)
    @Query("DELETE FROM vacancy_table WHERE hhID = :vacancyId")
    fun deleteVacancyFromFavourite(vacancyId: String)
    @Query("SELECT * FROM vacancy_table")
    fun getAllFavouritesVacancies(): Flow<List<DetailVacancyEntity>>

    @Query("SELECT hhID FROM vacancy_table")
    fun getAllFavouritesVacanciesId(): List<String>
    @Query("SELECT * FROM vacancy_table WHERE hhID = :id LIMIT 1")
    fun getVacancyById(id: String): DetailVacancyEntity?
}
