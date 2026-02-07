package ru.practicum.android.diploma.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.DictionariesInteractor
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.industry.IndustryUiState

class CountryViewModel (
    val dictionariesInteractor: DictionariesInteractor,
    val filterInteractor: FilterInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<IndustryUiState>()
    private var latestSearchText: String? = null
    private var industriesList: List<Industry>? = null
    fun observeState(): LiveData<IndustryUiState> = stateLiveData
}
