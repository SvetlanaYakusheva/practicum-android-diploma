package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FavoriteVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SharingRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesRepository
import ru.practicum.android.diploma.domain.api.SearchVacanciesRepository
import ru.practicum.android.diploma.data.sharing.SharingRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository
import ru.practicum.android.diploma.domain.api.VacancyDetailsRepository

val repositoryModule = module {

    single {
        SearchVacanciesRepositoryImpl(
            networkClient = get(),
            mapper = get()
        )
    }

    single<SearchVacanciesRepository> {
        get<SearchVacanciesRepositoryImpl>()
    }

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get(), get(), get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get(), get())
    }

    single<FavoriteVacanciesRepository> {
        FavoriteVacanciesRepositoryImpl(get(), get())
    }
}
