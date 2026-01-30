package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository

val repositoryModule = module {

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get(), get())
    }
}
