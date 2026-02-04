package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.VacancySource
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }
    viewModel { (vacancyId: String, sourceFragment: VacancySource) ->
        VacancyDetailsViewModel(vacancyId, sourceFragment, get(), get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }
}
