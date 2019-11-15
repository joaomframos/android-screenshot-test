package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helppiertestlibrary.HelppierAPIKT
import android.util.Log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val helpInstance = HelppierAPIKT("Hello");
        helpInstance.s(this);
        //Log.i("Main", HelppierAPI.i);
        // HelppierApi.
        // HelppierAPI.s(this)
        // ToasterMessage.s(this, "Hello")
    }
}
