package com.example.part11_32

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val typeTest = TypeTest()
    val controlTest = ControlTest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        typeTestBtn.setOnClickListener(this)
        controlTestBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0 == typeTestBtn){
            resultTextView.setText("${typeTest.testType()} \n ${typeTest.testArray()} \n ${typeTest.testAny("Hello")}")
        }else if(p0==controlTestBtn){
            resultTextView.setText(("${controlTest.testIf(25)} \n ${controlTest.testWhen("http://www.google.com")} \n ${controlTest.testFor()}"))
        }
    }
}
