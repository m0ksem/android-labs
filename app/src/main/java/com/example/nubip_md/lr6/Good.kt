package com.example.nubip_md.lr6

class Good(
    var id: Int = 0, var name: String, var price: Int, var count: Int
) {
    override fun toString(): String {
        return "$name $price $count"
    }
}