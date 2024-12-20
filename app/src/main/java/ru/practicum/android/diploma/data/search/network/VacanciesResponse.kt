package ru.practicum.android.diploma.data.search.network

import ru.practicum.android.diploma.data.search.dto.VacancyDTO

data class VacanciesResponse(
    val items: List<VacancyDTO>,
    val found: Int,
    val page: Int,
    val pages: Int
) : Response()
