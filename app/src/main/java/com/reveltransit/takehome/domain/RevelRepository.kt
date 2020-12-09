package com.reveltransit.takehome.domain

import androidx.annotation.RawRes
import com.reveltransit.takehome.R
import com.reveltransit.takehome.helper.ResourceProvider
import com.reveltransit.takehome.model.VehiclesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RevelRepository @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

//    fun fetchVehicles(): VehiclesResponse {
//
//    }

    fun readStringFromFile(@RawRes rawId: Int) {
        val text = resourceProvider.getRawResourceAsString(R.raw.upstream_vehicles)
    }
}