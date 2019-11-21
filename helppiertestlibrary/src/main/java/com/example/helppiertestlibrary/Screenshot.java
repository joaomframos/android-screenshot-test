package com.example.helppiertestlibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class Screenshot {
    public void takeScreenshot(View view, ImageView screnshotView) {
        Date now = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        Log.i("TakeScreenshot", "Taking screenshot");
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            // View v1 = activity.getWindow().getDecorView().getRootView();
            // v1.setDrawingCacheEnabled(true) ;
            // Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            // v1.setDrawingCacheEnabled(false);

            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                    view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            screnshotView.setImageBitmap(bitmap);

            /*File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Log.i("TakeScreenShotEnd", "Taking screenshot succedeed");*/

            // openScreenshot(imageFile);
        } catch (Throwable e) {
            Log.i("TakeScreenShotFailed", "Taking screenshot failed");

            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
