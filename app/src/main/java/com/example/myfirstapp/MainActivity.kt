package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
// import com.example.helppiertestlibrary.HelppierAPIKT
// import android.util.Log
// import android.view.View
// import com.example.helppiertestlibrary.BannerDrawable
import com.example.helppiertestlibrary.Screenshot

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val screenshotC = findViewById<ImageView>(R.id.screenShot)

        val buttonListener = View.OnClickListener { view ->
            var scrn = Screenshot()
            val layoutView = findViewById<ConstraintLayout>(R.id.layout);
            scrn.takeScreenshot(layoutView, screenshotC)
        }

        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener(buttonListener)



        // var b = findViewById<View>(R.id.textview);
        // var a = this.getRootView()
        // var scrn = Screenshot()
        // Log.i("TakeScreenShotRequest", "Taking screenshot requested")

        // scrn.takeScreenshot(b)
        /*val button = findViewById<View>(R.id.button)
        val overlay = button.getOverlay()
        val bannerDrawable = BannerDrawable()
        var myRunnable: Runnable = object : Runnable {
            override fun run() {
                //top right square
                bannerDrawable.setBounds(
                    button.getWidth() / 2,
                    0,
                    button.getWidth(),
                    button.getHeight() / 2
                );
                overlay.add(bannerDrawable);
            }
        }


        button.post(myRunnable)*/
        // val helpInstance = HelppierAPIKT("Hello");
        //helpInstance.s(this);
        //Log.i("Main", HelppierAPI.i);
        // HelppierApi.
        // HelppierAPI.s(this)
        // ToasterMessage.s(this, "Hello")
    }
}
