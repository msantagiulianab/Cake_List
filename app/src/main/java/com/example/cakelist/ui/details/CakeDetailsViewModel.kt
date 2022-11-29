package com.example.cakelist.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cakelist.CakesRepository
import com.example.cakelist.cake_id
import com.example.cakelist.models.Cake
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CakeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: CakesRepository
) : ViewModel() {

    var cakeState = mutableStateOf<Cake?>(null)

    init {
        val cakeId = savedStateHandle.get<String>(cake_id)
        cakeState.value = repository.getCake(cakeId!!)
    }

}