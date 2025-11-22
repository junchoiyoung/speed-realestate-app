package org.speed.realestate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.speed.realestate.data.RealEstate
import org.speed.realestate.data.RealEstateRepository
import org.speed.realestate.data.Tag

class RealEstateViewModel(private val realEstateRepository: RealEstateRepository) : ViewModel() {

    fun add(realEstate: RealEstate) = viewModelScope.launch {
        realEstateRepository.addRealEstate(realEstate)
    }

    fun delete(realEstate: RealEstate) = viewModelScope.launch {
        realEstateRepository.deleteRealEstate(realEstate)
    }

    fun update(realEstate: RealEstate) = viewModelScope.launch {
        realEstateRepository.updateRealEstate(realEstate)
    }

    val realEstateNames: StateFlow<List<String>> =
        realEstateRepository.getNames().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val realEstateTypes: StateFlow<List<String>> =
        realEstateRepository.getTypes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val realEstateOptions: StateFlow<List<String>> =
        realEstateRepository.getOptions().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val realEstateDongs: StateFlow<List<String>> =
        realEstateRepository.getDongs().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun addTag(tag: Tag) = viewModelScope.launch {
        realEstateRepository.addTag(tag)
    }

    val tags: StateFlow<List<Tag>> =
        realEstateRepository.getTags().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val filterTags = MutableStateFlow<Map<String, String>>(emptyMap())

    fun updateFilterTags(tags: Map<String, String>) {
        filterTags.value = tags.toMap()
    }

    val filteredRealEstates: StateFlow<List<RealEstate>> =
        realEstateRepository.getAllRealEstates()
            .combine(filterTags) { allEstates, filter ->
                if (filter.isEmpty()) {
                    println("필터없음")
                    allEstates
                } else {
                    allEstates.filter { estate ->
                        filter.all { (category, value) ->
                            val filterValues =
                                value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            if (filterValues.isEmpty()) return@all true

                            when (category) {
                                "아파트명" -> filterValues.contains(estate.name)
                                "타입" -> filterValues.contains(estate.type)
                                "동" -> filterValues.contains(estate.dong)
                                "거래유형" -> filterValues.contains(estate.tradeType)
                                "옵션" -> {
                                    val estateOptions =
                                        estate.option?.split(",")?.map { it.trim() } ?: emptyList()
                                    filterValues.all { filterOption ->
                                        estateOptions.contains(
                                            filterOption
                                        )
                                    }
                                }

                                else -> true
                            }
                        }
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
}