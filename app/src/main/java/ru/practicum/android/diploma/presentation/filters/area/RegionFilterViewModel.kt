package ru.practicum.android.diploma.presentation.filters.area

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filters.area.AreaFilterInteractor
import ru.practicum.android.diploma.domain.filters.area.model.AreaFilterModel
import ru.practicum.android.diploma.domain.filters.area.model.Region
import ru.practicum.android.diploma.domain.filters.filters.api.ControlFiltersInteractor
import ru.practicum.android.diploma.presentation.SingleEventLiveData
import java.util.Locale

class RegionFilterViewModel(
    private val interactor: AreaFilterInteractor,
    private val filtersInteractor: ControlFiltersInteractor
) : ViewModel() {

    private val regionsList = mutableListOf<Region>()
    private var country = AreaFilterModel()
    private var searchText = EMPTY_STRING
    private var searchJob: Job? = null


    private val regionsListState = MutableLiveData<Pair<List<Region>?, Int?>>()
    private val screenExitTrigger = SingleEventLiveData<Boolean>()
    fun getRegionsListState(): LiveData<Pair<List<Region>?, Int?>> = regionsListState
    fun getScreenExitTrigger(): LiveData<Boolean> = screenExitTrigger

    fun getSearchText(searchText: String) {
        if (searchText.isNotBlank() && (this.searchText != searchText)) {
            this.searchText = searchText
            searchDebounce(SEARCH_DEBOUNCE_DELAY)
        }
    }

    fun clearScreen(flag: Boolean) {
        if (flag) {
            regionsListState.postValue(Pair(null, null))
            searchJob?.cancel()
        }
    }

    private fun searchDebounce(delay: Long) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delay)
            search()
        }
    }

    private fun search() {
        val regex = searchText.toRegex()
        val filteredList = regionsList.filter { region -> region.name.lowercase(Locale.ROOT).contains(regex) }
        if (filteredList.isNotEmpty()) {
            regionsListState.postValue(Pair(filteredList, null))
        } else {
            regionsListState.postValue(Pair(null, NOTHING_FOUND))
        }
    }

    fun setRegion(region: Region) {
        val newRegion = AreaFilterModel(region.id, region.name)
        Log.d("MY", region.name)

        getCountry(region.parentId)
        filtersInteractor.saveAreaCityFilter(country, newRegion)
        exitScreen()
    }

    private fun getCountry(parentId: String?) {
        viewModelScope.launch {
            interactor.getCountriesList().collect { pair ->
                val list = pair.first?.filter { region: Region -> region.id == parentId }
                if (list.isNullOrEmpty()) AreaFilterModel() else AreaFilterModel(list[ZERO].id, list[ZERO].name)
            }
        }
    }

    fun getAreaList() {
        viewModelScope.launch {
            interactor.getAllRegions().collect { pair ->
                when (pair.first) {
                    null -> {
                        regionsListState.postValue(Pair(null, pair.second))
                    }

                    else -> {
                        regionsList.clear()
                        regionsList.addAll(pair.first as List<Region>)
                        regionsListState.postValue(Pair(pair.first, null))
                    }
                }
            }
        }
    }

    fun exitScreen() {
        screenExitTrigger.postValue(true)
    }

    private companion object {
        const val EMPTY_STRING = ""
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val NOTHING_FOUND = 0
        const val ZERO = 0
    }
}
