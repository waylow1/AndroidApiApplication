package com.example.myapplication.url

import android.content.Context

class EnvGetter {

    fun getEnv(context: Context): String {
        val assetManager = context.assets
        var url="";
        try {
            val inputStream = assetManager.open(".env")
            val bufferedReader = inputStream.bufferedReader()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    url= it.split("=")[1]
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return url;
    }

}