package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val interactor: VacancyDetailsInteractor
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val vacancyDetailsState = MutableLiveData<VacancyDetailsState>()
    fun observeVacancyDetailsState(): LiveData<VacancyDetailsState> = vacancyDetailsState

    fun onFavoriteClicked() {
        _isFavorite.value = !_isFavorite.value
    }

    fun setInitialFavoriteState(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }

    fun fillData() {
        viewModelScope.launch {
            vacancyDetailsState.postValue(VacancyDetailsState.Loading)
            processResult(interactor.getVacancyById(vacancyId))
        }
    }

    private fun processResult(result: Resource<Vacancy>) {
        val state = when (result) {
            is Resource.Success -> VacancyDetailsState.Content( result.data)
            is Resource.Error -> {
                if (result.errorType == ErrorType.NothingFound) {
                    VacancyDetailsState.VacancyNotFoundError
                } else {
                    VacancyDetailsState.VacancyServerError
                }
            }
        }
        vacancyDetailsState.postValue(state)
    }
}
