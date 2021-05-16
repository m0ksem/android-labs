package com.example.nubip_md.sr2

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.nubip_md.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import java.util.*


class Route : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    private lateinit var gMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var geoApiContext: GeoApiContext


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapViewFragment100) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationService = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)

        geoApiContext =
            GeoApiContext.Builder()
                .apiKey("AIzaSyCM9Y0xPU7Hc3dC42XXpWwZ5PeeFStJpwk")
                .build()
    }

    fun locToCoords(view: View) {
        val dialogEdit = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setTitle("Enter Coordinates")
            .setView(dialogEdit)
            .setPositiveButton("Confirm") { _: DialogInterface, _: Int ->
                val vals = dialogEdit.text.split(',')
                if (vals.size != 2) {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("Wrong coordinates")
                        .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                        .show()
                    return@setPositiveButton
                }

                val latLng = LatLng(vals[0].toDouble(), vals[1].toDouble())

                val loc = Location("Destination")
                loc.latitude = latLng.latitude
                loc.longitude = latLng.longitude

                val distanceInKM = currentLocation.distanceTo(loc) / 1000

                MaterialAlertDialogBuilder(this)
                    .setMessage("Distance to (${loc.latitude}, ${loc.longitude}) is $distanceInKM km.")
                    .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                    .show()
            }
            .setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
            .show()
    }

    fun locToAddress(view: View) {
        val dialogEdit = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setMessage("Enter address")
            .setView(dialogEdit)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                val address = dialogEdit.text.toString()

                val addresses = Geocoder(this).getFromLocationName(address, 1)
                if (addresses.size == 0) {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("No addresses found")
                        .setNeutralButton("OK") { _: DialogInterface, _: Int -> }
                        .show()
                    return@setPositiveButton
                }

                val pos = LatLng(addresses[0].latitude, addresses[0].longitude)

                val loc = Location("Destination")
                loc.latitude = pos.latitude
                loc.longitude = pos.longitude
                val distanceInKm = currentLocation.distanceTo(loc) / 1000

                MaterialAlertDialogBuilder(this)
                    .setMessage("Distance to $address is $distanceInKm km.")
                    .show()
            }
            .setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
            .show()
    }

    fun routeToAddress(view: View) {
        val dialogEdit = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setMessage("Enter address")
            .setView(dialogEdit)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                val geocoder = Geocoder(this, Locale.getDefault())
                val sourceAddresses = geocoder.getFromLocation(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    1
                )
                val destAddresses = geocoder.getFromLocationName(dialogEdit.text.toString(), 1)

                if (sourceAddresses.size == 0 || destAddresses.size == 0) {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("Can't find source or destination address")
                        .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                        .show()
                    return@setPositiveButton
                }

                val req = DirectionsApi.getDirections(
                    geoApiContext,
                    "${currentLocation.latitude},${currentLocation.longitude}",
                    "${destAddresses[0].latitude},${destAddresses[0].longitude}"
                ).await()

                val opts = PolylineOptions()
                opts.color(Color.parseColor("purple"))
                opts.width(8f)
                opts.visible(true)

                for (r in req.routes[0].overviewPolyline.decodePath()) {
                    opts.add(LatLng(r.lat, r.lng))
                }

                if (req.routes.isEmpty()) {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("Can't build route to your destination")
                        .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                        .show()
                    return@setPositiveButton
                }

                val coords = req.routes[0].overviewPolyline.decodePath()
                val lastCoordinates = coords[coords.size - 1]

                gMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(lastCoordinates.lat, lastCoordinates.lng))
                        .title(dialogEdit.text.toString())
                )
                gMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLocation.latitude, currentLocation.longitude)))
                gMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                gMap.addPolyline(opts)
            }
            .setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
            .show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.moveCamera(CameraUpdateFactory.zoomTo(5f))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(50.827780446558904, -0.6523635981089551)))
        gMap.addMarker(
            MarkerOptions()
                .position(LatLng(51.50080218936098, -0.12457468424459636))
                .title("Big Ben")
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        gMap.isMyLocationEnabled = true
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = location
    }
}