package com.example.nubip_md

import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

typealias Matrix = MutableList<MutableList<Int>>

class CalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val filed = findViewById<LinearLayout>(R.id.calculation_filed)
        val firstTable = TableLayout(this)
        val secondTable = TableLayout(this)

        renderTable(firstTable,3, 3)
        renderTable(secondTable,3, 3)

        findViewById<Button>(R.id.calculate_matrix).setOnClickListener {
            val operation = findViewById<EditText>(R.id.operation).text.toString()
            val matrix1 = readMatrixFromTable(firstTable)
            val matrix2 = readMatrixFromTable(secondTable)

            if (operation == "+") {
                showResult(addTwoMatrix(matrix1, matrix2))
            }
            if (operation == "-") {
                showResult(subTwoMatrix(matrix1, matrix2))
            }
            if (operation == "*") {
                showResult(mulTwoMatrix(matrix1, matrix2))
            }
            if (operation == "D") {
                findViewById<TextView>(R.id.result_text_view).text = determinantMatrix(matrix1).toString()
            }
        }

        filed.addView(firstTable)
        filed.addView(secondTable)
    }

    private fun addTwoMatrix(matrix1: Matrix, matrix2: Matrix): Matrix {
        val matrix: Matrix = arrayListOf()
        for(i in matrix1.indices){
            matrix.add(arrayListOf())
            for(j in matrix1[i].indices){
                matrix[i].add(matrix1[i][j]+matrix2[i][j])
            }
        }
        return matrix
    }

    private fun subTwoMatrix(matrix1: Matrix, matrix2: Matrix): Matrix {
        val matrix: Matrix = arrayListOf()
        for(i in matrix1.indices){
            matrix.add(arrayListOf())
            for(j in matrix1[i].indices){
                matrix[i].add(matrix1[i][j] - matrix2[i][j])
            }
        }
        return matrix
    }

    private fun mulTwoMatrix(matrix1: Matrix, matrix2: Matrix): Matrix {
        val matrix: Matrix = arrayListOf()
        for (i in 0 until matrix1.size) {
            matrix.add(arrayListOf())
            for (j in 0 until matrix1[0].size) {
                matrix[i].add(0)
                for (k in 0 until matrix1[0].size) {
                    matrix[i][j] += matrix1[i][k] * matrix2[k][j]
                }
            }
        }

        return matrix
    }

    fun johnsonTrotter(n: Int): Pair<List<IntArray>, List<Int>> {
        val p = IntArray(n) { it }  // permutation
        val q = IntArray(n) { it }  // inverse permutation
        val d = IntArray(n) { -1 }  // direction = 1 or -1
        var sign = 1
        val perms = mutableListOf<IntArray>()
        val signs = mutableListOf<Int>()

        fun permute(k: Int) {
            if (k >= n) {
                perms.add(p.copyOf())
                signs.add(sign)
                sign *= -1
                return
            }
            permute(k + 1)
            for (i in 0 until k) {
                val z = p[q[k] + d[k]]
                p[q[k]] = z
                p[q[k] + d[k]] = k
                q[z] = q[k]
                q[k] += d[k]
                permute(k + 1)
            }
            d[k] *= -1
        }

        permute(0)
        return perms to signs
    }

    fun determinantMatrix(m: Matrix): Double {
        val (sigmas, signs) = johnsonTrotter(m.size)
        var sum = 0.0
        for ((i, sigma) in sigmas.withIndex()) {
            var prod = 1.0
            for ((j, s) in sigma.withIndex()) prod *= m[j][s]
            sum += signs[i] * prod
        }
        return sum
    }

    private fun showResult(matrix: Matrix) {
        findViewById<TextView>(R.id.result_text_view).text = matrixToString(matrix)
    }

    private fun readMatrixFromTable(table: TableLayout): MutableList<MutableList<Int>> {
        var matrix: Matrix = arrayListOf()
        for (tr in table.children) {
            if (tr is TableRow) {
                val trArray: MutableList<Int> = arrayListOf()
                for (input in tr.children) {
                    if (input is EditText) {
                        val text = input.text.toString()
                        val number = text.toIntOrNull()

                        if (number == null) {
                            Toast.makeText(this, "Pizda", Toast.LENGTH_LONG)
                            continue
                        }
                        trArray.add(number)
                    }
                }
                matrix.add(trArray)
            }
        }
        return matrix
    }

    private fun matrixToString(matrix: Matrix): String {
        var str = ""
        for (tr in matrix) {
            str += tr.toString()
            str += "\n"
        }
        return str
    }

    private fun createInput(): EditText {
        val t = EditText(this)
        t.minWidth = 128
        t.width = 128
        t.inputType = InputType.TYPE_CLASS_NUMBER
        t.keyListener = DigitsKeyListener()
        t.setBackgroundResource(R.drawable.border_background)
        return t
    }

    private fun renderTable(tableLayout: TableLayout, horizontalSize: Int, verticalSize: Int) {
        tableLayout.removeAllViews()
        for (v in 1..verticalSize) {
            val tr = TableRow(this)
            for (h in 1..horizontalSize) {
                tr.addView(this.createInput())
            }
            tableLayout.addView(tr)
        }
    }
}