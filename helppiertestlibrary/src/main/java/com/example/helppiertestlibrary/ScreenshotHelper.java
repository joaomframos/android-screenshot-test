package com.example.helppiertestlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScreenshotHelper extends ImageView {
    View view;
    ImageView imageView;
    Bitmap bitmap;

    public ScreenshotHelper(Context context, View view, Bitmap bitmap) {
        super(context);

        this.view = view;
        this.bitmap = bitmap;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.screenshot_helper, (ViewGroup)this.view, true);

        this.imageView = view.findViewById(R.id.screenshotHelper);

        this.renderBitmap();
    }

    private void renderBitmap() {
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        this.imageView.setImageBitmap(bitmap);
    }
}
