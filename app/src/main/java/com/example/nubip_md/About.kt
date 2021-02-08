package com.example.nubip_md

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val studentName = findViewById<TextView>(R.id.student_name)
        val specialtyName = findViewById<TextView>(R.id.specialty_name)
        val courseAndGroup = findViewById<TextView>(R.id.course_and_group)
        val faculty = findViewById<TextView>(R.id.faculty)

        // Here some API request mock

        studentName.text = "Nedoshev Maksim"
        specialtyName.text = "Software engineering"
        courseAndGroup.text = "ipz19008bsk 2 st"
        faculty.text = "Information Technology"
    }
}