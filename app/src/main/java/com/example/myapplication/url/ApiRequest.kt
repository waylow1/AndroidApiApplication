package com.example.myapplication.url

import android.content.Context

class ApiRequest {

    public fun connectionTest():String{
        var url =  EnvGetter.getEnv()
        return url;
    }

}
