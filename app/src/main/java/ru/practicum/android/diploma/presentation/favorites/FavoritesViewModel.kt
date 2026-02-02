package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.ui.favorites.FavoritesUiState

class FavoritesViewModel : ViewModel() {
    private val favoritesState = MutableLiveData<FavoritesUiState>()
    fun observeFavoritesState(): LiveData<FavoritesUiState> = favoritesState

    fun fillData() {
        favoritesState.postValue(FavoritesUiState.Empty)
    }
}
