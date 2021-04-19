package com.example.nubip_md.lr10

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.nubip_md.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Geocode : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geocode)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapViewFragment10) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.button100).setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogEdit = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setTitle("Enter Address")
            .setView(dialogEdit)
            .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                val address = dialogEdit.text.toString()

                val addresses = Geocoder(this).getFromLocationName(address, 1)
                if (addresses.size == 0) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("No addresses found")
                        .setMessage("Wrong address name.")
                        .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                        .show()
                    return@setPositiveButton
                }

                val pos = LatLng(addresses[0].latitude, addresses[0].longitude)

                gMap.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title(addresses[0].getAddressLine(0))
                )

                gMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
                gMap.moveCamera(CameraUpdateFactory.zoomTo(13f))
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
}