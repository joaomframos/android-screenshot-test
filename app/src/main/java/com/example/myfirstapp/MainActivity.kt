package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helppiertestlibrary.HelppierAPI


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HelppierAPI.s(this, "Hello")
        // ToasterMessage.s(this, "Hello")
    }
}
