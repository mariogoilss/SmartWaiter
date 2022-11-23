package com.example.smartwaiter.inteface

data class Organization (var orgCif:String,
                         var orgName: String,
                         var orgFoodList:ArrayList<MenuItem>,
                         var orgDrinkList:ArrayList<MenuItem>,
                         var orgOpenOrNot:Boolean,
                         var orgSalesList:ArrayList<SalesList>,
                         var orgBankAccount:BankAccount)
