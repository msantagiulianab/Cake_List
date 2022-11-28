package com.example.cakelist.ui.cakes

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cakelist.CakesRepository
import com.example.cakelist.models.Cake
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CakesViewModel  @Inject constructor(
    val repository: CakesRepository
) : ViewModel() {

    /**
     * SEARCH
     */
    val query = mutableStateOf("")

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    // TODO
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