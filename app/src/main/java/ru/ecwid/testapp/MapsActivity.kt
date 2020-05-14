package ru.ecwid.testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val args: MapsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val list = args.list
        val builder = LatLngBounds.Builder()
        list.forEach {
            val coordinates = LatLng(it.lat, it.lon)
            val marker = map.addMarker(MarkerOptions().position(coordinates).title(it.address))
            builder.include(marker.position)
        }

        map.setOnMapLoadedCallback {
            val bounds = builder.build()
            val mapPadding = 50
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mapPadding)
            val maxZoom = 17.0f
            map.setMaxZoomPreference(maxZoom)
            map.moveCamera(cameraUpdate)
        }
    }
}
