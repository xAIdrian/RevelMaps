package com.reveltransit.takehome.model

data class VehiclesResponse(
    val vehicles: ArrayList<Vehicle> = ArrayList()
)

data class Vehicle(
    val id: String = "",
    val licensePlat: String = "",
    val sensors: Sensors?
)

data class Sensors(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val batteryLevel: Int
)