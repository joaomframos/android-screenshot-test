package com.example.helppiertestlibrary

import android.content.Context
import android.util.Log
// import android.app.Activity
// import android.view.View
import android.widget.Toast
// import androidx.fragment.app.FragmentManager
// import android.widget.LinearLayout
// import androidx.fragment.app.FragmentActivity

class HelppierAPIKT(tokenId: String) {
    val i = 5
    private val clientTokenId: String = tokenId
    fun s(c: Context) {
        Log.i("MyActivity", "test log")
        Toast.makeText(c, "test loga", Toast.LENGTH_SHORT).show()

        /*val rowLayout = LinearLayout(c)
        rowLayout.setId(View.generateViewId())
        // val a = c as Activity
        // a.setContentView(R.layout.btnscreenshot)
        // val fragmentManager = supportFragmentManager
        // val fragmentTransaction = FragmentManager.beginTransaction()
        FragmentActivity.getSupportFragmentManager()
        val myFragment = ExampleFragment()
        fragmentTransaction.add(, myFragment)
        fragmentTransaction.commit()*/

    }
}