package com.reveltransit.takehome.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.reveltransit.takehome.R
import com.reveltransit.takehome.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mapView by lazy { binding.mapView }
    private lateinit var symbolManager: SymbolManager


    override fun onCreate(savedInstanceState: Bundle?) {
        // in larger applications we'd put this in our base class
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView.onCreate(savedInstanceState)

        initMapView(mapView)
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
}