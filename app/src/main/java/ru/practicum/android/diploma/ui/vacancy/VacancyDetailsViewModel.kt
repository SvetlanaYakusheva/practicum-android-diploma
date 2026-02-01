package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VacancyDetailsViewModel : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun onFavoriteClicked() {
        _isFavorite.value = !_isFavorite.value
    }

    fun setInitialFavoriteState(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }
}
