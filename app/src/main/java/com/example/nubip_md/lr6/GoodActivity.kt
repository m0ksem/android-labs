package com.example.nubip_md.lr6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nubip_md.R

class CustomAdapter(private val dataSet: List<Good>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.good_name_value)
        val price: TextView = view.findViewById(R.id.good_price_value)
        val count: TextView = view.findViewById(R.id.good_count_value)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_good_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet[position].name
        viewHolder.price.text = dataSet[position].price.toString()
        viewHolder.count.text = dataSet[position].count.toString()
    }

    override fun getItemCount() = dataSet.size
}


class GoodActivity : AppCompatActivity() {
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_good)
        db = DBHelper(this)
        val goods = db.getAllGood()
        this.renderList(goods)

        findViewById<Button>(R.id.goods_count_filter_button).setOnClickListener {
            this.renderList(goods.filter { it.count < 5 })
        }
    }

    private fun renderList(goods: List<Good>) {
        var list = findViewById<RecyclerView>(R.id.goods_list)
        list.setHasFixedSize(false)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = CustomAdapter(goods)

        var max = goods.fold(Int.MIN_VALUE) { max, b -> if (b.price > max) b.price else max }
        var min = goods.fold(Int.MAX_VALUE) { min, b -> if (b.price < min) b.price else min }

        findViewById<TextView>(R.id.max_price_text_view).text = max.toString()
        findViewById<TextView>(R.id.min_price_text_view).text = min.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        this.db.db.close()
    }
}