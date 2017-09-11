package com.example.part11_33

import android.util.Log


fun printLog(str: String){
    Log.d("kkang", str)
}
open class User(name:String) {
    constructor(name: String, email: String):this(name)
    open var x: Int = 10
    open fun someFun(){
        printLog("Suer... someFun()")
    }
}

interface MyInterface {
    fun bar()
    fun foo() { }
}
open class Customer: User("kkang"), MyInterface{
    override var x: Int = 20
    override fun someFun() {
        super.someFun()
        printLog("Sub... ${super.x} .... $x")
    }
    override fun bar(){ }
}