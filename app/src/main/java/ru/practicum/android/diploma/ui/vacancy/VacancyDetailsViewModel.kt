package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val favoriteVacanciesInteractor: FavoriteVacanciesInteractor,

) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<VacancyDetailsState>()
    fun observePlayerState(): LiveData<VacancyDetailsState> = stateLiveData



    /*private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun onFavoriteClicked() {
        _isFavorite.value = !_isFavorite.value
    }

    fun setInitialFavoriteState(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }*/
}
