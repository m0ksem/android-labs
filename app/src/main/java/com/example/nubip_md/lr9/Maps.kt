package com.example.nubip_md.lr9

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
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

class Maps : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapViewFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.button9).setOnClickListener {
            showInputCoordsMessageDialog()
        }
    }

    private fun showInputCoordsMessageDialog() {
        val dialogEdit = EditText(this)
        MaterialAlertDialogBuilder(this)
                .setTitle("Enter coordinates")
                .setMessage("Latitude Longitude")
                .setView(dialogEdit)
                .setPositiveButton("Ok") { _: DialogInterface, _: Int ->
                    val vals = dialogEdit.text.split(',')
                    if (vals.size != 2) {
                        MaterialAlertDialogBuilder(this)
                                .setTitle("Error!")
                                .setMessage("Wrong coordinates")
                                .setNeutralButton("Ok") { _: DialogInterface, _: Int -> }
                                .show()
                        return@setPositiveButton
                    }

                    val loc = LatLng(vals[0].toDouble(), vals[1].toDouble())

                    gMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        gMap.isMyLocationEnabled = true
    }
}