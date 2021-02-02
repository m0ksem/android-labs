package com.example.nubip_md

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val studentName = findViewById<TextView>(R.id.student_name)
        val specialtyName = findViewById<TextView>(R.id.specialty_name)
        val courseAndGroup = findViewById<TextView>(R.id.course_and_group)
        val expectationText = findViewById<TextView>(R.id.expectation_text)

        // Here some API request mock

        studentName.text = "Nedoshev Maksim"
        specialtyName.text = "Software engineering"
        courseAndGroup.text = "ipz19008bsk 2 st"
        expectationText.text = "In this course, I would like to improve my knowledge " +
                "of Kotlin and make an interesting project at the end."
    }
}