package com.example.nubip_md

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.nubip_md.lr10.Geocode
import com.example.nubip_md.lr6.Auth
import com.example.nubip_md.lr7.AddressBook
import com.example.nubip_md.lr8.Gps
import com.example.nubip_md.lr9.Maps
import com.example.nubip_md.sr2.Route

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calculateButton = findViewById<Button>(R.id.calculate)

        calculateButton.setOnClickListener {
            val intent = Intent(this, Expression::class.java)
            startActivity(intent)
        }

        val aboutButton = findViewById<Button>(R.id.about)

        aboutButton.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

        val graphActivityButton = findViewById<Button>(R.id.graph_activity_button)

        graphActivityButton.setOnClickListener {
            val intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.open_photo_viewer_button).setOnClickListener {
            val intent = Intent(this, PhotoViewer::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.oepn_auth_activity).setOnClickListener {
            val intent = Intent(this, Auth::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.oepn_calculator_activity).setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.my_contacts_button).setOnClickListener {
            val intent = Intent(this, AddressBook::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.start_gps_activity).setOnClickListener {
            val intent = Intent(this, Gps::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.open_map).setOnClickListener {
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.open_geocode).setOnClickListener {
            val intent = Intent(this, Geocode::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.route_button).setOnClickListener {
            val intent = Intent(this, Route::class.java)
            startActivity(intent)
        }
    }
}