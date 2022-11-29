package com.example.cakelist

import com.example.cakelist.api.CakesApi
import com.example.cakelist.api.RetrofitHelper
import com.example.cakelist.models.Cake
import com.example.cakelist.sealed.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private fun fetchDataFromInternet() {
        _response.value = DataState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val cakesApi = RetrofitHelper.getInstance().create(CakesApi::class.java).getCakes()
            if (cakesApi.isSuccessful) {
                val listOfCakes = cakesApi.body()?.distinct()?.sortedBy { it.title }
                _response.value = DataState.Success(listOfCakes!!)
            } else {
                _response.value = DataState.Failure("Empty or null list")
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