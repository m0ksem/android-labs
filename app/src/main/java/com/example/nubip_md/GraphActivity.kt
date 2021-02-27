package com.example.nubip_md

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.io.BufferedReader
import java.io.InputStreamReader


class GraphActivity : AppCompatActivity() {
    private lateinit var openFileButton: Button
    private val OPEN_FILE_ACTIVITY_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        this.openFileButton = findViewById(R.id.open_file_with_graph)

        openFileButton.setOnClickListener {
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

    private fun parseFileText(text: String): MutableList<ExpressionResult> {
        val results: MutableList<ExpressionResult> = mutableListOf()

        text.split("\n").forEach {
            val regex = Regex("([0-9]+.[0-9]+|Infinity), ([0-9]+.[0-9]+|Infinity) = ([0-9]+.[0-9]+|Infinity)")
            val regexResult = regex.find(it) ?: return@forEach
            val (xString, yString, resultString) = regexResult.destructured

            val x = xString.toDouble()
            val y = yString.toDouble()
            val result = resultString.toDouble()
            results.add(ExpressionResult(x, y, result))
        }

        return results
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OPEN_FILE_ACTIVITY_CODE -> if (resultCode == Activity.RESULT_OK) {
                var uri = data?.data

                if (uri != null) {
                    val text = readResultFile(uri)
                    val results = parseFileText(text)
                    val graphView = findViewById<GraphView>(R.id.graph)

                    val dataPoints: List<DataPoint> = results.map {
                        DataPoint(it.x, it.result)
                    }

                    val series = LineGraphSeries(dataPoints.toTypedArray())

                    graphView.addSeries(series);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}