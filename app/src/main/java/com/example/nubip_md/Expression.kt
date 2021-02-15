package com.example.nubip_md

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow

class ExpressionResult(var x: Double, var y: Double, var result: Double) {
    override fun toString(): String {
        return "$x, $y = $result"
    }
}

class Expression : AppCompatActivity() {
    val FILE_SELECT_ACTIVITY_CODE = 0
    var calculationResult: String? = null
    var expressionResults: MutableList<ExpressionResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expression)

        val yInput = findViewById<EditText>(R.id.argument_y_input)
        val resultView = findViewById<TextView>(R.id.result)
        val saveResultToFile = findViewById<Button>(R.id.save_result_to_file)
        val startInput = findViewById<EditText>(R.id.argument_start_input)
        val stepInput = findViewById<EditText>(R.id.argument_step_input)
        val endInput = findViewById<EditText>(R.id.argument_end_input)

        saveResultToFile.visibility = View.GONE

        findViewById<Button>(R.id.calculate_button).setOnClickListener {
            val argumentY = yInput.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val argumentStart = startInput.text.toString().toIntOrNull() ?: return@setOnClickListener
            val argumentStep = stepInput.text.toString().toIntOrNull() ?: return@setOnClickListener
            val argumentEnd = endInput.text.toString().toIntOrNull() ?: return@setOnClickListener

            expressionResults = mutableListOf()
            calculationResult = "Result: "
            for (i in argumentStart..argumentEnd step argumentStep) {
                val result = calculateExpression(i.toDouble(), argumentY)

                expressionResults!!.add(ExpressionResult(i.toDouble(), argumentY, result))

                calculationResult += result.toString() + '\n'
            }

            resultView.text = calculationResult

            saveResultToFile.visibility = View.VISIBLE
        }

        saveResultToFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)

            try {
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_ACTIVITY_CODE)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateExpression(x: Double, y: Double): Double {
        val top = Math.E.pow(x.pow(3.0)) + cos(x - 4).pow(2.0)
        val bottom = atan(x) + 5.2 * y
        return top / bottom
    }

    private fun saveResultToFile(path: String = "/result.txt") {
        val file = File(path, "results.txt")

        file.createNewFile()

        var fileContent = "";

        for (result in this.expressionResults!!) {
            fileContent += "$result\n"
        }

        file.writeText(fileContent)

        Toast.makeText(this, "File saved to $path", Toast.LENGTH_LONG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_ACTIVITY_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                var path = data?.data?.path


//                var path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path

                if (path != null) {
                    saveResultToFile(path)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}