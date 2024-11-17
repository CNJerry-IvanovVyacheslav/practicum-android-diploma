package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.search.models.VacancyShort

interface SearchInteractor {
    fun search(request: String): Flow<Pair<List<VacancyShort>?, Int?>>
}