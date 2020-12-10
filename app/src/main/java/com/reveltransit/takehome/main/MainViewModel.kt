package com.reveltransit.takehome.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.reveltransit.takehome.dagger.ActivityScope
import com.reveltransit.takehome.domain.RevelRepository
import com.reveltransit.takehome.google.SingleLiveEvent
import com.reveltransit.takehome.model.Vehicle
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class MainViewModel @Inject constructor(
    private val revelRepository: RevelRepository
): ViewModel() {
    val vehicleMutableLiveData = MutableLiveData<FeatureCollection>()
    val itemClickEvent = SingleLiveEvent<Feature>()

    private var selectedFeature: Feature? = null

    fun getVehicles() {
        viewModelScope.launch {
            val vehicles = revelRepository.fetchVehicles()?.vehicles
            val symbolLayerIconFeatureList: ArrayList<Feature> = ArrayList()

            vehicles?.forEach { vehicle ->
                symbolLayerIconFeatureList.add(buildFeature(vehicle))
            }
            val featureCollection = FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
            vehicleMutableLiveData.postValue(featureCollection)
        }
    }

    private fun buildFeature(vehicle: Vehicle): Feature {
        val feature  = Feature.fromGeometry(Point.fromLngLat(
            vehicle.sensors?.lng ?: 0.0,
            vehicle.sensors?.lat ?: 0.0)
        )
        feature.addStringProperty(FEATURE_KEY_ID, vehicle.id)
        feature.addStringProperty(FEATURE_KEY_LICENSE_PLATE, vehicle.licensePlate)
        feature.addBooleanProperty(FEATURE_KEY_SELECTED, false)

        return feature
    }

    fun updateClickedFeature(clickedFeature: Feature?) {
        clickedFeature?.addBooleanProperty(
            FEATURE_KEY_SELECTED,
            !clickedFeature.getBooleanProperty(FEATURE_KEY_SELECTED)
        )
        selectedFeature = clickedFeature
    }

    fun startButtonClicked() {
        if (selectedFeature != null) {
            val workingFeature = selectedFeature
            when (workingFeature?.getStringProperty(FEATURE_KEY_RIDE_STATE)) {
                // ride has started and we can pause
                Vehicle.RIDING -> workingFeature.addStringProperty(FEATURE_KEY_RIDE_STATE, Vehicle.PAUSED)
                // ride is paused and we can resume aka start
                Vehicle.PAUSED -> workingFeature.addStringProperty(FEATURE_KEY_RIDE_STATE, Vehicle.RIDING)
                //
                Vehicle.RESERVED -> workingFeature.addStringProperty(FEATURE_KEY_RIDE_STATE, Vehicle.RIDING)
                // ride is open and can start
                null -> workingFeature?.addStringProperty(FEATURE_KEY_RIDE_STATE, Vehicle.RIDING)
            }
            selectedFeature = workingFeature
            itemClickEvent.value = selectedFeature
        }
    }

    fun endButtonClicked() {
        if (selectedFeature != null) {
            val workingFeature = selectedFeature
            when (workingFeature?.getStringProperty(FEATURE_KEY_RIDE_STATE)) {
                // ride has started and we are ending it!
                Vehicle.RIDING -> workingFeature.removeProperty(FEATURE_KEY_RIDE_STATE)
                // ride is paused and we are ending it!
                Vehicle.PAUSED -> workingFeature.removeProperty(FEATURE_KEY_RIDE_STATE)
                // ride is reserved and we are reserving it
                Vehicle.RESERVED -> workingFeature.removeProperty(FEATURE_KEY_RIDE_STATE)
                // ride is open and we are reserving it
                null -> workingFeature?.addStringProperty(FEATURE_KEY_RIDE_STATE, Vehicle.RESERVED)
            }
            selectedFeature = workingFeature
            itemClickEvent.value = selectedFeature
        }
    }

    fun canClearOrPickNewFeature() =
        selectedFeature?.getStringProperty(FEATURE_KEY_RIDE_STATE) != Vehicle.RIDING &&
                selectedFeature?.getStringProperty(FEATURE_KEY_RIDE_STATE) != Vehicle.PAUSED &&
                selectedFeature?.getStringProperty(FEATURE_KEY_RIDE_STATE) != Vehicle.RESERVED

    companion object {
        const val FEATURE_KEY_ID = "id"
        const val FEATURE_KEY_LICENSE_PLATE = "licensePlate"
        const val FEATURE_KEY_RIDE_STATE = "rideState"
        const val FEATURE_KEY_SELECTED = "selected"
    }
}