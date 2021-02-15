package com.example.nubip_md

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow

class ExpressionResult(var x: Double, var y: Double, var result: Double) {
    override fun toString(): String {
        return "$x, $y = $result"
    }
}

class Expression : AppCompatActivity() {
    private val SAVE_FILE_ACTIVITY_CODE = 0
    private val OPEN_FILE_ACTIVITY_CODE = 1
    var expressionResults: MutableList<ExpressionResult>? = null
    var resultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expression)

        val yInput = findViewById<EditText>(R.id.argument_y_input)
        val saveResultToFile = findViewById<Button>(R.id.save_result_to_file)
        val startInput = findViewById<EditText>(R.id.argument_start_input)
        val stepInput = findViewById<EditText>(R.id.argument_step_input)
        val endInput = findViewById<EditText>(R.id.argument_end_input)
        this.resultView = findViewById<TextView>(R.id.result)

        saveResultToFile.visibility = View.GONE

        findViewById<Button>(R.id.calculate_button).setOnClickListener {
            val argumentY = yInput.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            val argumentStart = startInput.text.toString().toIntOrNull() ?: return@setOnClickListener
            val argumentStep = stepInput.text.toString().toIntOrNull() ?: return@setOnClickListener
            val argumentEnd = endInput.text.toString().toIntOrNull() ?: return@setOnClickListener

            expressionResults = mutableListOf()
            var calculationResult = ""
            for (i in argumentStart..argumentEnd step argumentStep) {
                val result = calculateExpression(i.toDouble(), argumentY)

                expressionResults!!.add(ExpressionResult(i.toDouble(), argumentY, result))

                calculationResult += result.toString() + '\n'
            }

            resultView!!.text = calculationResult

            saveResultToFile.visibility = View.VISIBLE
        }

        saveResultToFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)

            try {
                startActivityForResult(Intent.createChooser(intent, "Select a where to save file"), SAVE_FILE_ACTIVITY_CODE)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.open_result_to_file).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, "text/plain")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

            try {
                startActivityForResult(intent,  OPEN_FILE_ACTIVITY_CODE)
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

    private fun saveResultToFile(uri: Uri) {
        val cr = contentResolver

        val outStream: OutputStream = cr.openOutputStream(uri) ?: return

        var fileContent = "";

        for (result in this.expressionResults!!) {
            fileContent += "$result\n"
        }

        outStream.write(fileContent.toByteArray())
        outStream.flush()
        outStream.close()

        Toast.makeText(this, "File saved", Toast.LENGTH_LONG)
    }

    private fun readResultFile(uri: Uri): String {
        val cr = contentResolver

        val stream = cr.openInputStream(uri) ?: return ""
        val inputStreamReader = InputStreamReader(stream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var resultS = ""
        var s: String?
        while (bufferedReader.readLine().also { s = it } != null) {
            resultS += "$s\n"
        }

        return resultS
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SAVE_FILE_ACTIVITY_CODE -> if (resultCode == Activity.RESULT_OK) {
                var uri = data?.data

                if (uri != null) {
                    val dir = DocumentFile.fromTreeUri(this, uri)
                    val file = dir?.createFile("text/plain", "result.txt")
                    val fileUri = file?.uri ?: return

                    saveResultToFile(fileUri)
                }
            }
            OPEN_FILE_ACTIVITY_CODE -> if (resultCode == Activity.RESULT_OK) {
                var uri = data?.data

                if (uri != null) {
                    if (resultView != null) {
                        resultView!!.text = readResultFile(uri)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}