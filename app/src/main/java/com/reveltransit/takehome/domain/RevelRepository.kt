package com.reveltransit.takehome.domain

import android.util.Log
import com.google.gson.Gson
import com.reveltransit.takehome.R
import com.reveltransit.takehome.helper.ResourceProvider
import com.reveltransit.takehome.model.VehiclesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RevelRepository @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    suspend fun fetchVehicles(): VehiclesResponse? {
        val gson = Gson()
        return try {
            gson.fromJson(readVehiclesFromFile(), VehiclesResponse::class.java)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "json exception with vehicle parse")
            null
        }
    }

    private fun readVehiclesFromFile() =
        resourceProvider
            .getRawResource(R.raw.upstream_vehicles)
            .bufferedReader()
            .readText()

    companion object {
        private val TAG = RevelRepository::class.simpleName
    }
}