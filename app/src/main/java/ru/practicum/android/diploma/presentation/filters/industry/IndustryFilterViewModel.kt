package ru.practicum.android.diploma.presentation.filters.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.vacancydetails.ErrorType
import ru.practicum.android.diploma.data.vacancydetails.Success
import ru.practicum.android.diploma.domain.filters.industry.api.IndustryFilterInteractor
import ru.practicum.android.diploma.domain.filters.industry.model.IndustryFilterModel
import ru.practicum.android.diploma.domain.filters.industry.model.IndustryFilterResult
import ru.practicum.android.diploma.presentation.filters.industry.model.IndustryFilterStates

class IndustryFilterViewModel(private val interactor: IndustryFilterInteractor) : ViewModel() {
    private val _stateIndustry: MutableLiveData<IndustryFilterStates> = MutableLiveData()
    fun observeIndustryState(): LiveData<IndustryFilterStates> = _stateIndustry

    private var selectedIndustry: IndustryFilterModel? = null
    private var allIndustries: List<IndustryFilterModel> = listOf()

    fun getIndustry() {
        renderState(IndustryFilterStates.Loading)
        viewModelScope.launch {
            interactor.getIndustries().collect { pair: Pair<IndustryFilterResult?, ErrorType> ->
                processResult(pair.first, pair.second)
            }
        }
    }
    private fun processResult(result: IndustryFilterResult?, errorType: ErrorType) {
        when (errorType) {
            is Success -> {
                if (result != null) {
                    allIndustries = result.industries
                    renderState(IndustryFilterStates.Content(result.industries))
                } else {
                    renderState(IndustryFilterStates.Empty)
                }
            }
            else -> {
                renderState(IndustryFilterStates.Error(errorType))
            }
        }
    }
    private fun renderState(state: IndustryFilterStates) {
        _stateIndustry.postValue(state)
    }
    fun selectIndustry(industry: IndustryFilterModel) {
        selectedIndustry = if (selectedIndustry == industry) null else industry
    }
    fun saveSelectedIndustry() {
        selectedIndustry?.let {
            interactor.saveIndustrySettings(it)
        }
    }

    fun searchIndustries(query: String) {
        val filteredIndustries = if (query.isEmpty()) {
            allIndustries
        } else {
            allIndustries.filter { it.name.contains(query, ignoreCase = true) }
        }
        renderState(IndustryFilterStates.Content(filteredIndustries))
    }
}
