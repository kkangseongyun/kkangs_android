package com.example.part11_32

import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Created by kkang on 2017-08-09.
 */
class ControlTest {
    fun testIf(no: Int): String{
        val result = if (no > 30) 30
        else if(no > 20) 20
        else 10

        return "testIf() : $result"
    }

    fun testWhen(x: Any): String {
        val result = when(x) {
            is String -> x.startsWith("http")
            else -> false
        }
        return "testWhen() : x is start with http - $result"
    }

    fun testFor(): String {
        val list = listOf("Hello", "World", "!")
        var result: String="testFor() : "
        for ((index, value) in list.withIndex()) {
            result += "$index is $value, "
        }
        return result
    }
}