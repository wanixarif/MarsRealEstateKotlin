/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsApiService
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

enum class MarsApiStatus{LOADING,DONE,ERROR}

class OverviewViewModel : ViewModel() {

    private val viewModelJob= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main+viewModelJob)

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status
    
    
    private val _properties=MutableLiveData<List<MarsProperty>>()
    val properties:LiveData<List<MarsProperty>>
        get()=_properties

    private var _navigateToSelectedProperty=MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty:LiveData<MarsProperty>
        get() = _navigateToSelectedProperty




    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)

    }



    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {

        uiScope.launch {
            var getPropertiesDeferred=MarsApi.retroFitService.getProperties(filter.value)
            try {
                MarsApiStatus.LOADING
                val listResult=getPropertiesDeferred.await()
                MarsApiStatus.DONE
                if (listResult?.size>0){
                    _properties.value=listResult
                }
            }
            catch (t: Throwable){
                _status.value=MarsApiStatus.ERROR

            }
        }


//        MarsApi.retroFitService.getProperties().enqueue(object :retrofit2.Callback<List<MarsProperty>>{
//            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//                _response.value="Failure"+t.message
//            }
//
//            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//                _response.value=response.body()?.size.toString()
//            }
//
//        })
    }


    fun displayPropertyDetails(marsProperty: MarsProperty){
        _navigateToSelectedProperty.value=marsProperty
    }

    fun displayPropertyDetailsDone(){
        _navigateToSelectedProperty.value=null
    }

    fun updateFilter(filter: MarsApiFilter){
        getMarsRealEstateProperties(filter)
    }





    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
