package ru.practicum.android.diploma.domain.search.models

import ru.practicum.android.diploma.util.EMPTY_STRING

data class VacancyShort(
    val vacancyId: String?,
    val name: String?,
    val employer: String? = EMPTY_STRING,
    val area: String? = EMPTY_STRING,
    val salary: String? = EMPTY_STRING,
    val currency: String? = EMPTY_STRING,
    val artLink: String? = EMPTY_STRING
)
