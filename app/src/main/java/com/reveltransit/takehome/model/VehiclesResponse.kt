package com.reveltransit.takehome.model

data class VehiclesResponse(
    val vehicles: ArrayList<Vehicle> = ArrayList()
)

data class Vehicle(
    val id: String = "",
    val licensePlate: String = "",
    val sensors: Sensors?,
    val rideState: VehicleRideState = VehicleRideState.OPEN
)

data class Sensors(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val batteryLevel: Double = 0.0
)

enum class VehicleRideState {
    OPEN,
    RESERVED,
    PAUSED,
    RIDING
}