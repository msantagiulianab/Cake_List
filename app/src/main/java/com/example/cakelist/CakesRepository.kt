package com.example.cakelist

import com.example.cakelist.api.CakesApi
import com.example.cakelist.api.RetrofitHelper
import com.example.cakelist.models.Cake
import com.example.cakelist.sealed.DataState
import com.google.firebase.database.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CakesRepository {

    private val _response: MutableStateFlow<DataState<List<Cake>, String>> =
        MutableStateFlow(DataState.Empty)
    val response = _response.asStateFlow()

    init {
        fetchDataFromInternet()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchDataFromInternet() {
        _response.value = DataState.Loading
        val quotesApi = RetrofitHelper.getInstance().create(CakesApi::class.java)
        GlobalScope.launch {
            val listOfCakes = quotesApi.getQuotes().body()?.distinct()?.sortedBy { it.title }
            if (listOfCakes.isNullOrEmpty()) {
                _response.value = DataState.Failure("Empty or null list")
            } else {
                _response.value = DataState.Success(listOfCakes)
            }
        }
    }

    /**
     * Get individual Cake for the details screen
     */
    fun getCake(title: String): Cake? {
        return (_response.value as? DataState.Success<List<Cake>>)?.data?.firstOrNull {
            it.title == title
        }
    }

}