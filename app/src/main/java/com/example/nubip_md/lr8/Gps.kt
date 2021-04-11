package com.example.nubip_md.lr8

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.nubip_md.R
import java.util.*

class Gps : AppCompatActivity(), LocationListener {
    private lateinit var locationService: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        locationService = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 123)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 123) {
            val fineLocationPermission = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
            val coarseLocationPermission = ActivityCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED

            if (!fineLocationPermission || !coarseLocationPermission) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                return
            }

            locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        findViewById<TextView>(R.id.latlng).text = "${location.latitude}, ${location.longitude}"
        findViewById<TextView>(R.id.address).text = addresses[0].getAddressLine(0)
        findViewById<TextView>(R.id.city).text = addresses[0].locality
    }
}