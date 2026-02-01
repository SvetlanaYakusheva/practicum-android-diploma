package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository

val repositoryModule = module {

    single {
        VacanciesRepositoryImpl(
            networkClient = get(),
            mapper = get()
        )
    }

    single<VacanciesRepository> {
        get<VacanciesRepositoryImpl>()
    }

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get(), get())
    }
}
