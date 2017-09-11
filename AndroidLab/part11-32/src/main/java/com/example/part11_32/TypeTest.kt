package com.example.part11_32

import android.util.Log

/**
 * Created by kkang on 2017-08-09.
 */
class TypeTest {

    fun testType(): String {

        val intData : Int = 10
        val result=intData.minus(5)

        var doubleData : Double = result.toDouble()//ok~~
        return "testType() : $doubleData"
    }
    fun testArray(): String {
        var array= arrayOf(1,"Hello",false)
        return "testArray() : ${array.size} .. ${array.get(2)}"
    }

    fun testAny(obj : Any): String{
        when(obj) {
            1 -> return "Int 1"
            "Hello" -> return "String Hello"
            else -> return "unknown"
        }
    }
}