package com.example.smartwaiter.clientBranch.basketUtils
import android.content.Context
import android.widget.Toast
import com.example.smartwaiter.inteface.MenuItem
import com.example.smartwaiter.inteface.SaleItem

class BasketUtils {
    companion object{

        var saleItemList = ArrayList<SaleItem>()

        fun saveSaleItemList(menuItem: MenuItem, context:Context){
            var amount:Int = 0
            var checker = true

            if(saleItemList.size > 0){
                for(i in 0 until saleItemList.size){
                    if (saleItemList[i].menuItem == menuItem){
                        saleItemList[i].amount = saleItemList[i].amount + 1
                        saleItemList[i].totalPrice = saleItemList[i].amount *  saleItemList[i].menuItem.price
                        checker = false
                    }
                }
                if (checker){
                    amount = 1
                    var saleItem = SaleItem(menuItem,amount, (menuItem.price*amount))
                    saleItemList.add(saleItem)
                }

            }else{
                amount = 1
                var saleItem = SaleItem(menuItem,amount, (menuItem.price*amount))
                saleItemList.add(saleItem)
            }


        }
    }
}