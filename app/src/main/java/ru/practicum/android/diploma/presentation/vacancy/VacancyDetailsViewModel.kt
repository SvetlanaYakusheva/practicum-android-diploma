package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FavoriteVacanciesInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.vacancy.VacancyDetailsState
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val vacancyDetailsInteractor: VacancyDetailsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val favoriteVacanciesInteractor: FavoriteVacanciesInteractor
) : ViewModel() {

    /*private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite*/

    private val vacancyDetailsState = MutableLiveData<VacancyDetailsState>()
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsState

    private var vacancy: Vacancy? = null

    /*fun onFavoriteClicked() {
        _isFavorite.value = !_isFavorite.value
    }

    fun setInitialFavoriteState(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }*/

    fun fillData() {
        viewModelScope.launch {
            renderState(VacancyDetailsState.Loading)
            vacancyDetailsInteractor
                .getVacancyById(vacancyId)
                .collect { result ->
                    processResult(result)
                }
        }
    }

    fun shareVacancy() {
        sharingInteractor.shareVacancy(vacancyId)
    }

    fun openEmail(mailTo: String, vacancyName: String) {
        sharingInteractor.openEmail(mailTo, vacancyName)
    }

    fun callPhone(phoneNumber: String) {
        sharingInteractor.callPhone(phoneNumber)
    }

    private fun processResult(result: Resource<Vacancy>) {
        val state = when (result) {
            is Resource.Success -> VacancyDetailsState.Content(result.data)
            is Resource.Error -> {
                if (result.errorType == ErrorType.NothingFound) {
                    VacancyDetailsState.VacancyNotFoundError
                } else {
                    VacancyDetailsState.VacancyServerError
                }
            }
        }
        renderState(state)
    }

    fun onFavoriteButtonClicked(isFavorite: Boolean) {
        if (vacancy != null) {
            viewModelScope.launch {
                if (isFavorite) {
                    favoriteVacanciesInteractor.deleteFromFavoriteVacancies(vacancy!!)
                    renderState(VacancyDetailsState.FavoriteStatus(false))
                } else {
                    favoriteVacanciesInteractor.addToFavoriteVacancies(vacancy!!)
                    renderState(VacancyDetailsState.FavoriteStatus(true))
                }
            }
        }
    }

    private fun renderState(state: VacancyDetailsState) {
        vacancyDetailsState.postValue(state)
    }
}
