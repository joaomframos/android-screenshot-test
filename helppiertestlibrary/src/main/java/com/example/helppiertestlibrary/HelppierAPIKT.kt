package com.example.helppiertestlibrary

import android.content.Context
import android.util.Log
import android.app.Activity
import android.widget.Toast


class HelppierAPIKT(tokenId: String) {
    val i = 5
    private val clientTokenId: String = tokenId
    fun s(c: Context) {
        Log.i("MyActivity", "test log")
        Toast.makeText(c, "test log", Toast.LENGTH_SHORT).show()
        // val a = c as Activity
        // a.setContentView(R.layout.btnscreenshot)
    }
}