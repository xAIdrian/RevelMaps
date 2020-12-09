package com.reveltransit.takehome.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reveltransit.takehome.domain.RevelRepository
import com.reveltransit.takehome.model.Vehicle
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val revelRepository: RevelRepository
): ViewModel() {

    val vehicleMutableLiveData = MutableLiveData<List<Vehicle>>()

    fun getVehicles() {
//        revelRepository.
    }
}