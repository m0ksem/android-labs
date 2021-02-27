package com.example.nubip_md

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

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
    }
}