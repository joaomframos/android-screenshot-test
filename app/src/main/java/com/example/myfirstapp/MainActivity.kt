package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.helppiertestlibrary.HelppierApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutView = findViewById<View>(R.id.layout)
        Log.i("ID Target Btn", Integer.toString(R.id.clientTarget));

        HelppierApp("HELPPIER_FAKE_KEY",this, layoutView).init()

    }
}
