package com.reveltransit.takehome.main

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.reveltransit.takehome.R
import com.reveltransit.takehome.RevelApp
import com.reveltransit.takehome.databinding.ActivityMainBinding
import com.reveltransit.takehome.model.Vehicle
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val mapView by lazy { binding.mapView }

    private lateinit var binding: ActivityMainBinding
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
            addMarkersToMap(it)
        })
    }

    private fun addMarkersToMap(vehicles: List<Vehicle>) {
        val symbolLayerIconFeatureList: ArrayList<Feature> = ArrayList()
        vehicles.forEach { vehicle ->
            vehicle.sensors?.let {
                symbolLayerIconFeatureList.add(
                    Feature.fromGeometry(Point.fromLngLat(it.lng, it.lat))
                )
            }
        }
        mapView.getMapAsync { map ->
            map.getStyle { style ->
                val featureCollection = FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
                val source = style.getSourceAs<GeoJsonSource>(SOURCE_ID)
                source?.setGeoJson(featureCollection)
            }
        }
    }

    private fun initMapView(mapView: MapView) {
        mapView.getMapAsync { map ->
            map.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(mapView, map, style)
                setupAnnotations(symbolManager)
                initMarkerSymbolLayer(style)
            }
        }
    }

    private fun initMarkerSymbolLayer(style: Style) {
        style.addImage(
            ICON_ID,
            BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
        )
        style.addSource(GeoJsonSource(SOURCE_ID))
        style.addLayer(
            SymbolLayer(LAYER_ID, SOURCE_ID)
                .withProperties(
                    iconImage(ICON_ID),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true)
                )
        )
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
        private const val ICON_ID = "DEFAULT_ICON_ID"
        private const val SOURCE_ID = "GEO_SOURCE_ID"
        private const val LAYER_ID = "GEO_LAYER_ID"
    }
}