package com.example.smartwaiter.inteface

data class Organization (var orgName: String,
                         var orgCif:String,
                         var orgFoodList:ArrayList<MenuItem>,
                         var orgDrinkList:ArrayList<MenuItem>,
                         var orgOpenOrNot:Boolean,
                         var orgSalesList:ArrayList<SalesList>,
                         var orgBankAccount:BankAccount,
                         var orgSuggestionsMailBox:ArrayList<String>,
                         var orgTablesList:ArrayList<Int>)
