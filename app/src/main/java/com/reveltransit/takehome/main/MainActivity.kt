package com.reveltransit.takehome.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.reveltransit.takehome.R
import com.reveltransit.takehome.RevelApp
import com.reveltransit.takehome.databinding.ActivityMainBinding
import com.reveltransit.takehome.model.Vehicle
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mapView by lazy { binding.mapView }
    private lateinit var symbolManager: SymbolManager

    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // in larger applications we'd put this in our base class
        (applicationContext as RevelApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView.onCreate(savedInstanceState)

        initMapView(mapView)

        viewModel.getVehicles()
        viewModel.vehicleMutableLiveData.observe(this, Observer {
            addMarkersToMap(mapView, it)
        })
    }

    private fun addMarkersToMap(
        mapView: MapView,
        vehicles: List<Vehicle>
    ) {
        val markerList: ArrayList<MarkerOptions> = ArrayList()
        vehicles.forEach { vehicle ->
            vehicle.sensors?.let {
                    markerList.add(MarkerOptions().position(LatLng(it.lat, it.lng)))
            }
        }
        mapView.getMapAsync { map ->
            map.addMarkers(markerList)
        }
    }

    private fun initMapView(mapView: MapView) {
        mapView.getMapAsync { map ->
            map.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(mapView, map, style)
                setupAnnotations(symbolManager)
            }
        }
    }

    private fun setupAnnotations(manager: SymbolManager) {
        val options = SymbolOptions()
                .withLatLng(LatLng(40.6677759, -74.0122655))
                .withIconImage("rocket-15")
                .withIconSize(1.5f)

        manager.create(options)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    companion object {
        private const val SOURCE_ID = "GEO_SOURCE_ID"
        private const val LAYER_ID = "GEO_LAYER_ID"
    }
}