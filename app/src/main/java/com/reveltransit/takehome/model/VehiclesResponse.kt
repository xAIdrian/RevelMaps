package com.reveltransit.takehome.model

data class VehiclesResponse(
    val vehicles: ArrayList<Vehicle> = ArrayList()
)

data class Vehicle(
    val id: String = "",
    val licensePlate: String = "",
    val sensors: Sensors?,
    val rideState: String
) {
    companion object {
        const val RESERVED = "reserved"
        const val PAUSED = "paused"
        const val RIDING = "riding"
    }
}

data class Sensors(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val batteryLevel: Double = 0.0
)