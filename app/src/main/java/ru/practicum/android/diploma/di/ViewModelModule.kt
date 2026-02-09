package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.VacancySource
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.industry.IndustryViewModel
import ru.practicum.android.diploma.presentation.location.CountryViewModel
import ru.practicum.android.diploma.presentation.location.LocationViewModel
import ru.practicum.android.diploma.presentation.location.RegionViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel { (vacancyId: String, sourceFragment: VacancySource) ->
        VacancyDetailsViewModel(vacancyId, sourceFragment, get(), get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel<IndustryViewModel> {
        IndustryViewModel(
            dictionariesInteractor = get(),
            filterInteractor = get()
        )
    }

    viewModel<FilterViewModel> {
        FilterViewModel(
            get()
        )
    }

    viewModel<LocationViewModel> {
        LocationViewModel(get())
    }

    viewModel<CountryViewModel> {
        CountryViewModel(get(), get())
    }

    viewModel<RegionViewModel> {
        RegionViewModel(get(), get())
    }
}
