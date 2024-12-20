package ru.practicum.android.diploma.di.vacancydetails

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favorite.interactors.VacancyDetailFavoriteInteractor
import ru.practicum.android.diploma.domain.favorite.interactors.VacancyDetailFavoriteInteractorImpl
import ru.practicum.android.diploma.domain.vacancydetails.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.vacancydetails.impl.VacancyDetailsInteractorImpl

val vacancyDetailsInteractorModule = module {

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get(), get())
    }
    single<VacancyDetailFavoriteInteractor> { VacancyDetailFavoriteInteractorImpl(get()) }
}
