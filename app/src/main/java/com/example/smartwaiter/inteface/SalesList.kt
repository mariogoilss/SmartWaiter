package com.example.smartwaiter.inteface

import java.util.Date

data class SalesList (var date: String, var saleItemList:ArrayList<SaleItem>, var done:Boolean, var table:Long, var benefit:Double)