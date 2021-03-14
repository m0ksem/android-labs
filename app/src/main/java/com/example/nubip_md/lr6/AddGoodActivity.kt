package com.example.nubip_md.lr6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.nubip_md.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.concurrent.schedule


class AddGoodActivity : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_good)
        db = DBHelper(this)

        findViewById<Button>(R.id.add_good_button).setOnClickListener {
            db.addGood(createGood())
            this.finish()
        }
    }

    fun createGood(): Good {
        val name = findViewById<TextView>(R.id.add_good_name_input).text.toString()
        val price = findViewById<TextView>(R.id.add_good_price_input).text.toString().toInt()
        val count = findViewById<TextView>(R.id.add_good_count_input).text.toString().toInt()
        return Good(name = name, count = count, price = price )
    }
}