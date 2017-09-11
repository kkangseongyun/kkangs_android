package com.example.part11_33

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inheritanceTestBtn.setOnClickListener(this)
        propertyTestBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0==inheritanceTestBtn){
            var customer=Customer()
            customer.someFun()
        }else if(p0==propertyTestBtn){
            var obj=PropertyClass()
            obj.greeting="kkang"
            resultTextView.setText("Property Test \n greeting : ${obj.greeting}, name : ${obj.name}")
        }
    }
}
