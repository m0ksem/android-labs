package com.example.nubip_md.lr6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.nubip_md.About
import com.example.nubip_md.R

class Auth : AppCompatActivity() {
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        db = DBHelper(this)
        db.init()

        findViewById<Button>(R.id.auth_button).setOnClickListener {
            if (db.checkUser(this.username, this.password)) {
                val intent = Intent(this, GoodActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val username: String
        get() {
            return findViewById<AutoCompleteTextView>(R.id.username_input).text.toString()
        }

    private val password: String
        get() {
            return findViewById<AutoCompleteTextView>(R.id.password_input).text.toString()
        }

    override fun onDestroy() {
        super.onDestroy()
        this.db.db.close()
    }

}