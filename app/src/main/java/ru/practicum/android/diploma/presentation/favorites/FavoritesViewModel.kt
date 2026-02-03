package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.favorites.FavoritesUiState
import ru.practicum.android.diploma.util.Resource

class FavoritesViewModel(
    private val interactor: FavoriteVacanciesInteractor
) : ViewModel() {
    private val favoritesState = MutableLiveData<FavoritesUiState>()
    fun observeFavoritesState(): LiveData<FavoritesUiState> = favoritesState

    fun fillData() {
        favoritesState.postValue(FavoritesUiState.Empty)
        viewModelScope.launch {
            interactor
                .getFavoriteVacancies()
                .collect { result ->
                    processResult(result)
                }
        }

    }

    private fun processResult(result: Resource<List<Vacancy>>) {
        val state = when (result) {
            is Resource.Success -> {
                if (result.data.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Content(result.data)
                }
            }

            is Resource.Error -> {
                FavoritesUiState.Error
            }
        }
        favoritesState.postValue(state)
    }
}
