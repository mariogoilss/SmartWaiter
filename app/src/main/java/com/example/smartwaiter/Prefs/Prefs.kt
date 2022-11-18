package com.example.smartwaiter.Prefs

import android.content.Context

class Prefs (val context: Context) {

    val SHARED_NAME = "Mydtb"
    val SHARED_EMAIL = "email"
    val SHARED_RECOR = "recor"


    //CREAMOS LA BASE DE DATOS
    val storage = context.getSharedPreferences(SHARED_NAME, 0)


    /*
     * FUN GUARDA VALORES
     */
    fun saveCorreo(email:String){
        storage.edit().putString(SHARED_EMAIL,email).apply()
    }

    fun saveRecordar(recor:Boolean){
        storage.edit().putBoolean(SHARED_RECOR, recor).apply()
    }



    /*
     * FUN DEVUELVE VALORES
     */

    fun getCorreo():String{
        return storage.getString(SHARED_EMAIL, "")!!
    }

    fun getRecor():Boolean{
        return  storage.getBoolean(SHARED_RECOR, false)
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}