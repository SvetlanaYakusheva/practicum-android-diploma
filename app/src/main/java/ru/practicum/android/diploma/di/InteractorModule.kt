package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.DictionariesInteractorImpl
import ru.practicum.android.diploma.data.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    single<SearchVacanciesInteractor> {
        SearchVacanciesInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(
            get()
        )
    }

    single<FavoriteVacanciesInteractor> {
        FavoriteVacanciesInteractorImpl(get())
    }

    single<DictionariesInteractor> {
        DictionariesInteractorImpl(
            repository = get(),
        )
    }

    single<FilterInteractor> {
        FilterInteractorImpl(
            repository = get(),
        )
    }

}
