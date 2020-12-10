package com.reveltransit.takehome.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.expressions.Expression.eq
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.reveltransit.takehome.R
import com.reveltransit.takehome.RevelApp
import com.reveltransit.takehome.databinding.ActivityMainBinding
import com.reveltransit.takehome.main.MainViewModel.Companion.FEATURE_KEY_LICENSE_PLATE
import com.reveltransit.takehome.main.MainViewModel.Companion.FEATURE_KEY_RIDE_STATE
import com.reveltransit.takehome.main.MainViewModel.Companion.FEATURE_KEY_SELECTED
import com.reveltransit.takehome.model.Vehicle
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private val mapView by lazy { binding.mapView }

    private lateinit var binding: ActivityMainBinding
    private lateinit var symbolManager: SymbolManager
    private val viewMap: HashMap<String?, View?>? = HashMap()

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // in larger applications we'd put this in our base class
        (applicationContext as RevelApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView.onCreate(savedInstanceState)

        initMapView(mapView)
        initButtonViews()

        viewModel.getVehicles()
        viewModel.vehicleMutableLiveData.observe(this, Observer {
            addMarkerFeaturesToMap(it)
        })
        viewModel.itemClickEvent.observe(this, Observer {
            when(it?.getProperty(FEATURE_KEY_RIDE_STATE)?.asString) {
                Vehicle.PAUSED -> {
                    binding.rideStatus.text = String.format(getString(R.string.status_format), Vehicle.PAUSED.capitalize())
                    binding.rideButton.text = getString(R.string.start_ride)
                    binding.rideButton.setBackgroundColor(getColor(R.color.goGreen))
                }
                Vehicle.RESERVED -> {
                    binding.rideStatus.text = String.format(getString(R.string.status_format), Vehicle.RESERVED.capitalize())
                    binding.alternateButton.text = getString(R.string.cancel_reservation)
                    binding.alternateButton.setBackgroundColor(getColor(R.color.cancelReservationCyan))
                }
                Vehicle.RIDING -> {
                    binding.rideStatus.text = String.format(getString(R.string.status_format), Vehicle.RIDING.capitalize())

                    binding.rideButton.text = getString(R.string.paused)
                    binding.alternateButton.text = getString(R.string.end)

                    binding.rideButton.setBackgroundColor(getColor(R.color.pauseSky))
                    binding.alternateButton.setBackgroundColor(getColor(R.color.endRed))
                }
                else -> {
                    // this is null and our ride has ended
                    binding.rideStatus.text = String.format(getString(R.string.status_format), getString(R.string.nah))

                    binding.rideButton.text = getString(R.string.start_ride)
                    binding.alternateButton.text = getString(R.string.reserve)

                    binding.rideButton.setBackgroundColor(getColor(R.color.goGreen))
                    binding.alternateButton.setBackgroundColor(getColor(R.color.reserveBlue))
                }
            }
        })
    }

    private fun initButtonViews() {
        binding.rideButton.setOnClickListener {
            viewModel.startButtonClicked()
        }
        binding.alternateButton.setOnClickListener {
            viewModel.endButtonClicked()
        }
    }

    private fun addMarkerFeaturesToMap(vehiclesMarkers: FeatureCollection) {
        mapView.getMapAsync { map ->
            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>(SOURCE_ID)
                source?.setGeoJson(vehiclesMarkers)
            }
        }
    }

    private fun initMapView(mapView: MapView) {
        mapView.getMapAsync { map ->
            map.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(mapView, map, style)
                setupAnnotations(symbolManager)
                initMarkerSymbolLayer(style)
                initImageCalloutLayer(style)
            }
            map.addOnMapClickListener { point ->
                if (viewModel.canClearOrPickNewFeature()) {
                    val geoCoord = map.projection.toScreenLocation(point)
                    val markerFeaturesList = map.queryRenderedFeatures(geoCoord, LAYER_ID)

                    if (markerFeaturesList.isNotEmpty()) {
                        val selectedFeature = markerFeaturesList[0]
                        binding.rideId.text = selectedFeature.getStringProperty(FEATURE_KEY_LICENSE_PLATE)
                        viewModel.updateClickedFeature(selectedFeature)
                    } else {
                        binding.rideId.text = getString(R.string.no_rides)
                        viewModel.updateClickedFeature(null)
                    }
                }
                true
            }
        }
    }

    private fun initImageCalloutLayer(style: Style) {
        style.addLayer(
            SymbolLayer(CALLOUT_LAYER_ID, SOURCE_ID)
                .withProperties(
                    iconImage("{title}"),
                    iconAnchor(ICON_ANCHOR_BOTTOM),
                    iconAllowOverlap(true)
                ).withFilter(eq(get(FEATURE_KEY_SELECTED), true))
        )
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
        private const val CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID"
    }
}