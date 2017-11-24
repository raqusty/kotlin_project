package com.example.admin.kotlindownloader

/**
 * Created by linzehao
 * time: 2017/11/24.
 * info:
 */
class ssss private constructor(){
    companion object {
        fun get():ssss{
            return Inner.saaaa
        }
    }

    private object Inner{
        val saaaa = ssss()
    }
}
