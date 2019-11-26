package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.helppiertestlibrary.HelppierApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutView = findViewById<LinearLayout>(R.id.layout)

        HelppierApp("HELPPIER_FAKE_KEY",this, layoutView).init()
    }
}
