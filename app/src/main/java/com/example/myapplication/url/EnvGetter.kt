package com.example.myapplication.url


import io.github.cdimascio.dotenv.dotenv

class EnvGetter {

    companion object{
        public fun  getEnv(): String {
            val dotenv = dotenv {
                directory = "/assets"
                filename = "env"
            }
            return dotenv["url"];
        }
    }


}