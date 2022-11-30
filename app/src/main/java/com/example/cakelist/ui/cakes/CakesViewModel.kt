package com.example.cakelist.ui.cakes

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakelist.CakesRepository
import com.example.cakelist.models.Cake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CakesViewModel @Inject constructor(
    private val repository: CakesRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadCakes() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000L)
            repository.fetchDataFromInternet()
            _isLoading.value = false
        }
    }

    /**
     * SEARCH
     */
    val query = mutableStateOf("")

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    val cakesState = repository.response

    val results = mutableStateOf(0)

    fun countQuery(cakes: List<Cake>) {
        this.results.value = cakes.count {
            it.title.startsWith(query.value)
        }
    }

    /**
     * SORTING
     */
    val selected = mutableStateOf(false)

    fun changeSelection() {
        selected.value = !selected.value
    }

    /**
     * SCROLLING
     */
    val positionListState = mutableStateOf(LazyListState(0, 0))

    fun changePositionListState(firstItem: Int) {
        positionListState.value = LazyListState(firstItem, 0)
    }

}