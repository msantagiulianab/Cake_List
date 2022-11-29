package com.example.cakelist

import com.example.cakelist.api.CakesApi
import com.example.cakelist.api.RetrofitHelper
import com.example.cakelist.models.Cake
import com.example.cakelist.sealed.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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


////        private var movieLiveData = MutableLiveData<List<Result>>()
////        fun getPopularMovies() {
//           cakesApi.getCakes() .getPopularMovies("69d66957eebff9666ea46bd464773cf0").enqueue(object  : Callback<Movies>{
//                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
//                    if (response.body()!=null){
//                        movieLiveData.value = response.body()!!.results
//                    }
//                    else{
//                        return
//                    }
//                }
//                override fun onFailure(call: Call<Movies>, t: Throwable) {
//                    Log.d("TAG",t.message.toString())
//                }
//            })
//        }
//        fun observeMovieLiveData() : LiveData<List<Result>> {
//            return movieLiveData
//        }


//        val customersListLiveData: MutableLiveData<ArrayList<CustomerDataModel>> = MutableLiveData<ArrayList<CustomerDataModel>>()
//
//        CoroutineScope(Dispatchers.Default).launch {
//
//            launch(Dispatchers.IO) {
//                val apiInterface = RetrofitHelper.getInstance().create(CakesApi::class.java)
//                var response = apiInterface.getQuotes().body()?.distinct()?.sortedBy { it.title }
//                withContext(Dispatchers.Default)
//                {
//                    response?.let {
//                        if (response.isSuccessful()) {
//                            customersListLiveData.postValue(response.body()!!.data)
//                        }
//
//                    }
//                }
//            }
//
//        }

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