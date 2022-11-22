package com.example.smartwaiter.inteface

data class MenuItem(
    var name:String,
    var description:String, var price: Double, var allergens:ArrayList<Int>, var image:String)
