package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    single<SearchVacanciesInteractor> {
        SearchVacanciesInteractorImpl(get())
    }
    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }
}
