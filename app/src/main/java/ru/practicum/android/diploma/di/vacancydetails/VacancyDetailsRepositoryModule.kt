package ru.practicum.android.diploma.di.vacancydetails

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.vacancydetails.api.VacancyDetailsRepository
import ru.practicum.android.diploma.domain.vacancydetails.impl.VacancyDetailsRepositoryImpl

val vacancyDetailsRepositoryModule = module {

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get())
    }
}