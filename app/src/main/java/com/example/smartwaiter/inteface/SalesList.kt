package com.example.smartwaiter.inteface

import java.util.Date

data class SalesList (var date: Date, var saleItemList:ArrayList<SaleItem>, var done:Boolean, var table:Int, var benefit:Double)