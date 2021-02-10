package com.example.nubip_md

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow

class Expression : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expression)

        val xInput = findViewById<EditText>(R.id.argument_x_input)
        val yInput = findViewById<EditText>(R.id.argument_y_input)
        val resultView = findViewById<TextView>(R.id.result)

        findViewById<Button>(R.id.calculate_button).setOnClickListener {
            val argumentX = xInput.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val argumentY = yInput.text.toString().toDoubleOrNull() ?: return@setOnClickListener

            resultView.text = "Result: " + calculateExpression(argumentX, argumentY).toString()
        }
    }

    private fun calculateExpression(x: Double, y: Double): Double {
        val top = Math.E.pow(x.pow(3.0)) + cos(x - 4).pow(2.0)
        val bottom = atan(x) + 5.2 * y
        return top / bottom
    }
}