package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val vacancyDetailsState = MutableLiveData<VacancyDetailsState>()
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private var isFavoriteStatusLoaded = false

    private var vacancy: Vacancy? = null

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
            is Resource.Success -> {
                vacancy = result.data
                if (!isFavoriteStatusLoaded) favoriteStatus()
                VacancyDetailsState.Content(vacancy!!)
            }

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

    private fun favoriteStatus() {
        viewModelScope.launch {
            favoriteVacanciesInteractor
                .getFavoriteVacanciesIds()
                .collect { ids ->
                    processFavoriteStatus(ids)
                }
        }
    }

    private fun processFavoriteStatus(ids: List<String>) {
        if (vacancy != null) {
            vacancy!!.isFavorite = ids.contains(vacancy!!.id)
            _isFavorite.value = vacancy!!.isFavorite
        }
    }

    fun onFavoriteButtonClicked() {
        if (vacancy != null) {
            viewModelScope.launch {
                if (vacancy!!.isFavorite) {
                    favoriteVacanciesInteractor.deleteFromFavoriteVacancies(vacancy!!)
                } else {
                    favoriteVacanciesInteractor.addToFavoriteVacancies(vacancy!!)
                }
            }
        }
    }

    private fun renderState(state: VacancyDetailsState) {
        vacancyDetailsState.postValue(state)
    }
}
