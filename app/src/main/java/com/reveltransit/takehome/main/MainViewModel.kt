package com.reveltransit.takehome.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reveltransit.takehome.domain.RevelRepository
import com.reveltransit.takehome.model.Vehicle
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val revelRepository: RevelRepository
): ViewModel() {

    val vehicleMutableLiveData = MutableLiveData<List<Vehicle>>()

    fun getVehicles() {
        viewModelScope.launch {
            vehicleMutableLiveData.postValue(revelRepository.fetchVehicles()?.vehicles)
        }
    }
}