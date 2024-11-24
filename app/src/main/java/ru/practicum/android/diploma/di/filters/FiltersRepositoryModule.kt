package ru.practicum.android.diploma.di.filters

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.filters.industry.api.IndustryFilterRepository
import ru.practicum.android.diploma.domain.filters.industry.impl.IndustryFilterRepositoryImpl
import ru.practicum.android.diploma.data.filters.filters.FiltersControlRepositoryImpls
import ru.practicum.android.diploma.domain.filters.filters.repository.FiltersControlRepository

val FiltersRepositoryModule = module {
    single<IndustryFilterRepository> {
        IndustryFilterRepositoryImpl(get(named("industry")))
    }
    single<FiltersControlRepository> { FiltersControlRepositoryImpls(get(), get()) }
}
